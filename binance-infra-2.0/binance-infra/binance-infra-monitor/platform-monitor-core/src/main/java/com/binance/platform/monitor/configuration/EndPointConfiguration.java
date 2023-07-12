package com.binance.platform.monitor.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.binance.platform.env.EnvCondition.DevCondition;
import com.binance.platform.monitor.endpoint.AppInfoEndPoint;
import com.binance.platform.monitor.endpoint.DocsEndpoint;
import com.binance.platform.monitor.endpoint.GcLogEndpoint;
import com.binance.platform.monitor.endpoint.GraySetEndpoint;
import com.binance.platform.monitor.endpoint.HeapDumpEndpoint;
import com.binance.platform.monitor.endpoint.JarDependenciesEndpoint;
import com.binance.platform.monitor.endpoint.JetCacheManagerEndpoint;
import com.binance.platform.monitor.endpoint.KickOutEndpoint;
import com.binance.platform.monitor.endpoint.LogFileRegistry;
import com.binance.platform.monitor.endpoint.MetricsInfoEndpoint;
import com.binance.platform.monitor.endpoint.RequestMappingEndpoint;
import com.binance.platform.monitor.endpoint.ServiceDependenciesEndpoint;
import com.binance.platform.monitor.endpoint.ThreadDumpEndpoint;
import com.binance.platform.monitor.health.HealthCheckAspect;
import com.binance.platform.monitor.health.SwithHealthCheck;

@Configuration
@EnableConfigurationProperties(LogFileRegistry.class)
public class EndPointConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public JarDependenciesEndpoint jarDependenciesEndpoint() {
        return new JarDependenciesEndpoint(env);
    }

    @Bean
    @ConditionalOnMissingBean
    public GcLogEndpoint gcLogEndpoint() {
        return new GcLogEndpoint();
    }

    @Bean
    public HeapDumpEndpoint heapDumpEndpoint() {
        return new HeapDumpEndpoint();
    }

    @Bean
    @ConditionalOnWebApplication
    public ThreadDumpEndpoint threadDumpEndpoint() {
        return new ThreadDumpEndpoint();
    }

    @Bean
    @ConditionalOnWebApplication
    public AppInfoEndPoint appInfoEndPoint() {
        return new AppInfoEndPoint();
    }

    @Bean
    @ConditionalOnWebApplication
    public MetricsInfoEndpoint metricsInfoEndpoint() {
        return new MetricsInfoEndpoint();
    }

    @Bean
    @ConditionalOnWebApplication
    public ServiceDependenciesEndpoint serviceDependenciesEndpoint() {
        return new ServiceDependenciesEndpoint();
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    public DocsEndpoint docsEndpoint(Environment env) {
        return new DocsEndpoint(env);
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    @Conditional(DevCondition.class)
    public KickOutEndpoint kickOutEndpoint() {
        return new KickOutEndpoint();

    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    @Conditional(DevCondition.class)
    public RequestMappingEndpoint requestMappingEndpoint() {
        return new RequestMappingEndpoint();
    }

    @Bean
    @ConditionalOnBean(type = {"org.springframework.data.redis.connection.RedisConnectionFactory"})
    @ConditionalOnMissingBean
    public JetCacheManagerEndpoint jetCacheManagerEndpoint() {
        return new JetCacheManagerEndpoint();
    }

    @Bean
    public SimpleInfoContributor springBootVersionInfoContributor() {
        return new SimpleInfoContributor("spring-boot-version", SpringBootVersion.getVersion());
    }

    @Bean
    @ConditionalOnWebApplication
    public GraySetEndpoint graySetEndpoint() {
        return new GraySetEndpoint();
    }

    @Bean
    public HealthCheckAspect healthCheckLog() {
        return new HealthCheckAspect();
    }

    @Bean
    public SwithHealthCheck SwithHealthCheck() {
        return new SwithHealthCheck();
    }

}
