
package com.binance.platform.ribbon;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@EnableConfigurationProperties
@AutoConfigureAfter(RibbonAutoConfiguration.class)
@RibbonClients(defaultConfiguration = MyRibbonClientConfiguration.class)
public class MyRibbonAutoConfiguration {

    @ConditionalOnMissingBean(ServerFilter.class)
    @Bean
    public HeaderGrayServerFilter headerGrayServerFilter(ConfigurableEnvironment env) {
        return new HeaderGrayServerFilter(env);
    }

}
