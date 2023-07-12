package com.binance.platform.resilience4j.bulkhead;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import com.binance.master.error.BusinessException;
import com.binance.platform.common.AlarmUtil;
import com.binance.platform.resilience4j.openfeign.FeignDecorators;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.vip.vjtools.vjkit.net.NetUtil;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.event.BulkheadOnCallRejectedEvent;
import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.micrometer.tagged.TaggedBulkheadMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.vavr.CheckedFunction1;

public class BinanceBulkheadRegister implements ConfigChangeListener, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(BinanceBulkheadRegister.class);
    private static final ConcurrentHashMap<String, Bulkhead> bulkHeadMap = new ConcurrentHashMap<>();

    @Value("${binance.resilience.bulkhead.isOpenBulkhead:true}")
    private volatile boolean isOpenBulkhead;
    @Value("${binance.resilience.bulkhead.default.maxConcurrent:25}")
    private int defaultMaxConcurrent;
    @Value("${binance.resilience.bulkhead.default.maxWaitMs:10}")
    private long defaultMaxWaitMs;

    private static final String PREFIX_BULKHEAD_CONFIG = "binance.resilience.bulkhead.";
    private static final String MAX_CONCURRENT = ".maxConcurrent";
    private static final String MAX_WAIT_MS = ".maxWaitMs";
    private static final String DEFAULT_FEIGN_NAME = "default";

    private ThreadPoolExecutor threadPoolExecutor;
    private ApplicationContext applicationContext;
    private MeterRegistry meterRegistry;
    private BulkheadRegistry bulkheadRegistry;

    public BinanceBulkheadRegister(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        try {
            bulkheadRegistry = BulkheadRegistry.ofDefaults();
            if (this.meterRegistry != null) {
                TaggedBulkheadMetrics.ofBulkheadRegistry(bulkheadRegistry).bindTo(this.meterRegistry);
            }
        } catch (Exception e) {
            logger.error("init meterRegistry or bulkheadRegistry error", e);
        }
    }

    @PostConstruct
    private void init() {
        ConfigService.getAppConfig().addChangeListener(this);
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2),
            new ThreadFactoryBuilder().setNameFormat("resilience4j-bulkhead-pool-%d").build(),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void withBulkhead(FeignDecorators.Builder builder, String feignClientName,
        ApplicationContext applicationContext) {
        Bulkhead bulkhead =
            bulkHeadMap.computeIfAbsent(feignClientName, (key) -> createBulkhead(feignClientName, applicationContext));
        builder.withBulkhead(bulkhead, this);
    }

    private Bulkhead createBulkhead(String feignClientName, ApplicationContext applicationContext) {
        BulkheadConfig config = loadBulkheadConfig(feignClientName, applicationContext);
        Bulkhead bulkhead = bulkheadRegistry.bulkhead(feignClientName, config);
        bulkhead.getEventPublisher().onCallRejected(new EventConsumer<BulkheadOnCallRejectedEvent>() {

            @Override
            public void consumeEvent(BulkheadOnCallRejectedEvent callRejectedEvent) {
                try {
                    Metrics.counter("resilience4j.bulkhead.rejected.count", "bulkhead",
                        callRejectedEvent.getBulkheadName()).increment();
                    int sampleValue = ThreadLocalRandom.current().nextInt(100);
                    if (sampleValue >= 10) {
                        return;
                    }
                    String rejectMessage =
                        String.format("The Current Service:[%s],IP:[%s],Downstream Service:[%s] bulkhead is Full!",
                            applicationContext.getEnvironment().getProperty("spring.application.name"),
                            NetUtil.getLocalHost(), callRejectedEvent.getBulkheadName());
                    AlarmUtil.alarmTeams(rejectMessage);
                } catch (Throwable e) {
                    logger.warn("fail to set monitor for bulkhead metric. bulkhead: {}",
                        callRejectedEvent.getBulkheadName());
                }
            }

        });
        return bulkhead;
    }

    private BulkheadConfig loadBulkheadConfig(String feignClientName, ApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        String maxConcurrent = env.getProperty(PREFIX_BULKHEAD_CONFIG + feignClientName + MAX_CONCURRENT);
        String maxWaitMs = env.getProperty(PREFIX_BULKHEAD_CONFIG + feignClientName + MAX_WAIT_MS);

        int maxConc = StringUtils.isNumeric(maxConcurrent) ? Integer.parseInt(maxConcurrent) : defaultMaxConcurrent;
        long maxWait = StringUtils.isNumeric(maxWaitMs) ? Long.parseLong(maxWaitMs) : defaultMaxWaitMs;

        return BulkheadConfig.custom().maxConcurrentCalls(maxConc).maxWaitDuration(Duration.ofMillis(maxWait)).build();
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        Set<String> changedFeignBulk = Sets.newHashSet();
        for (String key : changeEvent.changedKeys()) {
            if (StringUtils.startsWith(key, PREFIX_BULKHEAD_CONFIG)) {
                ConfigChange change = changeEvent.getChange(key);
                parseChangedFeign(changedFeignBulk, change);
            }
        }
        threadPoolExecutor.submit(() -> refreshConfig(changedFeignBulk, System.currentTimeMillis()));
    }

    private void parseChangedFeign(Set<String> changedFeignBulk, ConfigChange change) {
        if (!change.getPropertyName().endsWith(MAX_CONCURRENT) && !change.getPropertyName().endsWith(MAX_WAIT_MS)) {
            return;
        }
        if (change.getChangeType() == PropertyChangeType.MODIFIED
            || change.getChangeType() == PropertyChangeType.ADDED) {
            String feignName = getFeignName(change.getPropertyName());
            changedFeignBulk.add(feignName);
        }
    }

    private String getFeignName(String key) {
        String feignName =
            key.replace(PREFIX_BULKHEAD_CONFIG, StringUtils.EMPTY).replace(MAX_CONCURRENT, "").replace(MAX_WAIT_MS, "");
        return feignName;
    }

    private void refreshConfig(Set<String> changedFeignBulk, long eventTime) {
        if (CollectionUtils.isEmpty(changedFeignBulk)) {
            return;
        }
        try {
            // first refresh non default, then refresh default
            Set<String> feignNames = Sets.newHashSet(bulkHeadMap.keySet());
            if (changedFeignBulk.contains(DEFAULT_FEIGN_NAME)) {
                feignNames.remove(DEFAULT_FEIGN_NAME);
                changeBulkheadCfg(feignNames);
            } else {
                changeBulkheadCfg(changedFeignBulk);
            }
        } catch (Exception e) {
            logger.error("update bulkhead config error. names={}", changedFeignBulk);
        }
        logger.info("refresh bulkhead used time={}", (System.currentTimeMillis() - eventTime));
    }

    private void changeBulkheadCfg(Set<String> feignNames) {
        feignNames.forEach(feignName -> {
            Bulkhead bulkhead = bulkHeadMap.get(feignName);
            if (bulkhead != null) {
                bulkhead.changeConfig(loadBulkheadConfig(feignName, this.applicationContext));
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public interface BinanceBulkheadFun {
        /**
         * Returns a function which is decorated by a bulkhead.
         *
         * @param bulkhead
         *            the bulkhead
         * @param fun
         *            the original function
         * @param <T>
         *            the type of the input to the function
         * @param <R>
         *            the type of the result of the function
         * @return a function which is decorated by a bulkhead.
         */
        static <T, R> CheckedFunction1<T, R> decorateCheckedFunction(Bulkhead bulkhead, CheckedFunction1<T, R> fun,
            BinanceBulkheadRegister binanceBulkheadRegister) {
            return (T t) -> {
                if (!binanceBulkheadRegister.isOpenBulkhead) {
                    return fun.apply(t);
                }
                if (!bulkhead.tryAcquirePermission()) {
                    throw new BusinessException(com.binance.master.error.GeneralCode.SERVICE_HEAVY_LOAD);
                }
                try {
                    return fun.apply(t);
                } finally {
                    bulkhead.onComplete();
                }
            };
        }
    }
}
