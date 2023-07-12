
package com.binance.platform.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.common.ExcludeAutoConfigurationUtil;

public class ExcludeSpringBootRabbitAutoConfigProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String RABBIT_AUTOCONFIGURATION =
        "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration";

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ExcludeAutoConfigurationUtil.excludeAutoConfiguration(environment, new String[] {RABBIT_AUTOCONFIGURATION});
    }
}
