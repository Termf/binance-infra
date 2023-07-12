package com.binance.platform.resilience4j;

import static com.ctrip.framework.apollo.enums.PropertyChangeType.MODIFIED;

import com.binance.platform.resilience4j.ddos.DdosWhiteList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.binance.platform.resilience4j.bulkhead.BinanceBulkheadRegister;
import com.binance.platform.resilience4j.circuitbreaker.client.BnCircuitBreakerAspect;
import com.binance.platform.resilience4j.circuitbreaker.server.BusinessCircuitBreakerInterceptor;
import com.binance.platform.resilience4j.ddos.DdosRateLimiterInterceptor;
import com.binance.platform.resilience4j.openfeign.BinaceResilience4jFeign;
import com.binance.platform.resilience4j.ratelimiter.IpRateLimiterStrategy;
import com.binance.platform.resilience4j.ratelimiter.UserIdAndIPRateLimiterStrategy;
import com.binance.platform.resilience4j.ratelimiter.UserIdRateLimiterStrategy;
import com.binance.platform.resilience4j.ratelimiter.client.BnRateLimiterAspect;
import com.binance.platform.resilience4j.ratelimiter.server.BusinessRateLimiterInterceptor;
import com.binance.platform.resilience4j.timeLimiter.client.BnTimeLimiterAspect;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;

import io.github.resilience4j.utils.AspectJOnClasspathCondition;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class Resilience4jApolloAutoConfiguration {

    @Autowired
    private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @Configuration
    @ConditionalOnWebApplication
    static class ProviderSupportConfig extends WebMvcConfigurerAdapter {

        @Value("${server.error.path:${error.path:/error}}")
        private String errorPath;

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private DdosWhiteList ddosWhiteList;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new BusinessRateLimiterInterceptor(applicationContext)).addPathPatterns("/**")
                .excludePathPatterns("/**" + errorPath);
            registry.addInterceptor(new DdosRateLimiterInterceptor(applicationContext, ddosWhiteList)).addPathPatterns("/**")
                .excludePathPatterns("/**" + errorPath);
            registry.addInterceptor(new BusinessCircuitBreakerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/**" + errorPath);
        }

    }

    @Bean
    public IpRateLimiterStrategy ipRateLimiterStrategy() {
        return new IpRateLimiterStrategy();
    }

    @Bean
    public DdosWhiteList infraDdosWhiteList() {
        return new DdosWhiteList();
    }

    @Bean
    public UserIdRateLimiterStrategy userIdRateLimiterStrategy() {
        return new UserIdRateLimiterStrategy();
    }

    @Bean
    public UserIdAndIPRateLimiterStrategy userIdAndIPRateLimiterStrategy() {
        return new UserIdAndIPRateLimiterStrategy();
    }

    @Bean
    public CommandLineRunner resilience4jCommandLineRunner() {

        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {
                Config configApplication = ConfigService.getAppConfig();
                configApplication.addChangeListener(new ConfigChangeListener() {

                    @Override
                    public void onChange(ConfigChangeEvent changeEvent) {
                        for (String key : changeEvent.changedKeys()) {
                            if (StringUtils.containsIgnoreCase(key, "resilience4j")) {
                                ConfigChange change = changeEvent.getChange(key);
                                if (change.getChangeType() == MODIFIED) {
                                    refreshScope.refreshAll();
                                    break;
                                }
                            }
                        }

                    }

                });
            }

        };
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public BinaceResilience4jFeign.Builder feignResilience4jBuilder() {
        return BinaceResilience4jFeign.builder();

    }

    @Bean
    @Conditional(AspectJOnClasspathCondition.class)
    public BnTimeLimiterAspect bntimeLimiterAspect() {
        return new BnTimeLimiterAspect();
    }

    @Bean
    @Conditional(AspectJOnClasspathCondition.class)
    public BnCircuitBreakerAspect bnCircuitBreakerAspect() {
        return new BnCircuitBreakerAspect();
    }

    @Bean
    @Conditional(AspectJOnClasspathCondition.class)
    public BnRateLimiterAspect bnRateLimiterAspect() {
        return new BnRateLimiterAspect();
    }

    @Bean
    public BinanceBulkheadRegister binanceBulkheadRegister(@Autowired(required = false) MeterRegistry meterRegistry) {
        return new BinanceBulkheadRegister(meterRegistry);
    }

}
