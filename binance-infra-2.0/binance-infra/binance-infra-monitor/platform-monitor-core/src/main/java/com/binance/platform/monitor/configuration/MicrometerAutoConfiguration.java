package com.binance.platform.monitor.configuration;

import java.util.List;
import java.util.Map;

import org.apache.catalina.Manager;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.web.tomcat.TomcatMetricsBinder;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.alicp.jetcache.anno.support.CacheMonitorManager;
import com.binance.platform.common.FilterOrder;
import com.binance.platform.monitor.logging.aop.GenericControllerAspect;
import com.binance.platform.monitor.logging.tracing.TracingOncePerRequestFilter;
import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.DataSourceBeanPostProcessor;
import com.binance.platform.monitor.metric.MyRegistryCustomizer;
import com.binance.platform.monitor.metric.RedisCommandBeanPostProcessor;
import com.binance.platform.monitor.metric.db.p6spy.SQLLogger;
import com.binance.platform.monitor.metric.hazelcast.HazelCastInstanceMetrics;
import com.binance.platform.monitor.metric.jetcacche.JetCacheMetrics;
import com.binance.platform.monitor.metric.micrometer.MicrometerBinder;
import com.binance.platform.monitor.metric.tomcat.TomcatH2cMetricsBinder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.cache.HazelcastCacheMetrics;

@Configuration
public class MicrometerAutoConfiguration {

    @Configuration
    @AutoConfigureOrder(0)
    @AutoConfigureBefore(value = {RedisAutoConfiguration.class, DataSourceAutoConfiguration.class})
    public static class MetrcisUtilAutoConfiguration {

        @Bean
        public CustomizerTag platformTag() {
            return new CustomizerTag();
        }

        @Bean
        @ConditionalOnClass(name = {"org.springframework.data.redis.core.RedisTemplate"})
        public RedisCommandBeanPostProcessor redisCommandBeanPostProcessor() {
            return new RedisCommandBeanPostProcessor();
        }

        @Bean
        @Conditional(DataSourceConditon.class)
        public DataSourceBeanPostProcessor DataSourceBeanPostProcessor(CustomizerTag tag, Environment env,
            ApplicationEventPublisher applicationEventPublisher) {
            SQLLogger.applicationEventPublisher = applicationEventPublisher;
            return new DataSourceBeanPostProcessor(tag);
        }

        public static class DataSourceConditon implements Condition {
            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                return isPresent("com.zaxxer.hikari.HikariDataSource")
                    || isPresent("com.alibaba.druid.pool.DruidDataSource");
            }

        }

        private static boolean isPresent(String className) {
            try {
                Class.forName(className);
                return true;
            } catch (Throwable ex) {
                return false;
            }
        }

    }

    @Configuration
    @ConditionalOnClass({HazelcastInstance.class})
    @ConditionalOnBean(IMap.class)
    public static class HazelcastCacheConfiguration {
        @Bean
        public HazelcastCacheMetricsConfiguration hazelcastCacheMetricsConfiguration(Map<String, IMap> iMaps,
            Map<String, HazelcastInstance> hazelcastInstanceMap, CustomizerTag platformTag) {
            return new HazelcastCacheMetricsConfiguration(iMaps, hazelcastInstanceMap, platformTag);
        }

    }

    @Configuration
    @ConditionalOnClass(name = {"com.alicp.jetcache.anno.support.CacheMonitorManager"})
    public static class JetCacheConfiguration {
        @Bean(initMethod = "init")
        public JetCacheMetrics jetCacheBeanPostProcessor(Map<String, CacheMonitorManager> cacheMonitorManagerMap,
            CustomizerTag platformTag) {
            return new JetCacheMetrics(cacheMonitorManagerMap, platformTag);
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    public static class WebMonitorConfiguration {

        @Bean
        public TracingOncePerRequestFilter tracingOncePerRequestFilter() {
            return new TracingOncePerRequestFilter();
        }

        @Bean
        public FilterRegistrationBean<TracingOncePerRequestFilter>
            tracingOncePerRequestFilterBean(TracingOncePerRequestFilter tracingOncePerRequestFilter) {
            FilterRegistrationBean<TracingOncePerRequestFilter> ret =
                new FilterRegistrationBean<TracingOncePerRequestFilter>();
            ret.setFilter(tracingOncePerRequestFilter);
            ret.addUrlPatterns("/*");
            ret.setOrder(FilterOrder.FILTERORDER_1);
            return ret;
        }

        @Bean
        public GenericControllerAspect genericControllerAspect() {
            return new GenericControllerAspect();
        }

    }

    @Bean
    public HealthMetricsConfiguration healthMetricsConfiguration(HealthAggregator healthAggregator,
        List<HealthIndicator> healthIndicators, CustomizerTag platformTag) {
        return new HealthMetricsConfiguration(healthAggregator, healthIndicators, platformTag);
    }

    @Bean
    public MicrometerBinder micrometerBinder() {
        return new MicrometerBinder();
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(CustomizerTag platformTag) {
        MyRegistryCustomizer ret = new MyRegistryCustomizer(platformTag);
        return ret;
    }

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect(Metrics.globalRegistry);
    }

    protected static class HazelcastCacheMetricsConfiguration {

        public HazelcastCacheMetricsConfiguration(Map<String, IMap> iMaps,
            Map<String, HazelcastInstance> hazelcastInstanceMap, CustomizerTag platformTag) {
            if (iMaps != null) {
                iMaps.forEach((k, v) -> {
                    HazelcastCacheMetrics.monitor(Metrics.globalRegistry, v, platformTag.getTags());
                });
            }
            if (hazelcastInstanceMap != null) {
                hazelcastInstanceMap.forEach((k, v) -> {
                    new HazelCastInstanceMetrics(v, k, platformTag).bindTo(Metrics.globalRegistry);
                });
            }
        }
    }

    protected static class HealthMetricsConfiguration {

        private static int getStatusCode(HealthIndicator healthIndicator) {
            switch (healthIndicator.health().getStatus().getCode()) {
                case "UP":
                    return 0;
                case "OUT_OF_SERVICE":
                    return 1;
                case "DOWN":
                    return 2;
                case "UNKNOWN":
                    return 3;
                default:
                    return 0;
            }
        }

        private CompositeHealthIndicator healthIndicator;

        public HealthMetricsConfiguration(HealthAggregator healthAggregator, List<HealthIndicator> healthIndicators,
            CustomizerTag platformTag) {
            healthIndicator = new CompositeHealthIndicator(healthAggregator);
            healthIndicators.forEach(h -> {
                Metrics.gauge("health." + h.getClass().getSimpleName().replace("HealthIndicator", "").toLowerCase(),
                    platformTag.getTags(), h, HealthMetricsConfiguration::getStatusCode);
                healthIndicator.addHealthIndicator(h.toString(), h);
            });
            Metrics.gauge("health", platformTag.getTags(), healthIndicator, HealthMetricsConfiguration::getStatusCode);
        }

    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass({Manager.class})
    public class TomcatH2cMetricsAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean({TomcatH2cMetricsBinder.class})
        public TomcatMetricsBinder tomcatH2cMetricsBinder(MeterRegistry meterRegistry) {
            return new TomcatH2cMetricsBinder(meterRegistry);
        }

    }
}