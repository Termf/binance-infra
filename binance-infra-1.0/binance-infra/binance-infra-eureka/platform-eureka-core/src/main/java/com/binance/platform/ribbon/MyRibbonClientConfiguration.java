
package com.binance.platform.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.ribbon.wrapper.ZoneAvoidanceAndGrayAndLoadBasedRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;

@Configuration
@ConditionalOnBean(SpringClientFactory.class)
public class MyRibbonClientConfiguration {
    @Value("${ribbon.client.name}")
    private String serviceId = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    public MyRibbonClientConfiguration() {}

    public MyRibbonClientConfiguration(String serviceId) {
        this.serviceId = serviceId;
    }

    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, serviceId)) {
            return this.propertiesFactory.get(IRule.class, config, serviceId);
        }
        ZoneAvoidanceAndGrayAndLoadBasedRule rule = new ZoneAvoidanceAndGrayAndLoadBasedRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }

}
