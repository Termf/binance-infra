package com.binance.platform.monitor.configuration.cloudwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration;
import org.springframework.cloud.aws.context.annotation.ConditionalOnMissingAmazonClient;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.binance.platform.env.EnvCondition.ProdCondition;
import com.binance.platform.monitor.configuration.cloudwatch.internal.CloudWatchConfig;
import com.binance.platform.monitor.configuration.cloudwatch.internal.CloudWatchMeterRegistry;

import io.micrometer.core.instrument.Clock;

@Configuration
@Import(ContextCredentialsAutoConfiguration.class)
@AutoConfigureBefore({CompositeMeterRegistryAutoConfiguration.class, SimpleMetricsExportAutoConfiguration.class})
@AutoConfigureAfter(MetricsAutoConfiguration.class)
@EnableConfigurationProperties(CloudWatchProperties.class)
@ConditionalOnProperty(prefix = "management.metrics.export.cloudwatch", name = "namespace")
@ConditionalOnClass({CloudWatchMeterRegistry.class, RegionProvider.class})
@Conditional(ProdCondition.class)
public class CloudWatchExportAutoConfiguration {

    @Autowired(required = false)
    private RegionProvider regionProvider;

    @Bean
    @ConditionalOnProperty(value = "management.metrics.export.cloudwatch.enabled", matchIfMissing = true)
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, Clock clock,
        AmazonCloudWatchAsync client) {
        return new CloudWatchMeterRegistry(config, clock, client);
    }

    @Bean
    @ConditionalOnMissingAmazonClient(AmazonCloudWatchAsync.class)
    public AmazonWebserviceClientFactoryBean<AmazonCloudWatchAsyncClient>
        amazonCloudWatchAsync(AWSCredentialsProvider credentialsProvider) {
        return new AmazonWebserviceClientFactoryBean<>(AmazonCloudWatchAsyncClient.class, credentialsProvider,
            this.regionProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public CloudWatchConfig cloudWatchConfig(CloudWatchProperties cloudWatchProperties) {
        return new CloudWatchPropertiesConfigAdapter(cloudWatchProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Clock micrometerClock() {
        return Clock.SYSTEM;
    }

}
