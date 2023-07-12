package com.binance.platform.monitor.configuration;

import com.binance.platform.monitor.metric.jms.KafkaMetricsBinder;
import org.apache.kafka.clients.KafkaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.jms.RabbitMetricsBinder;

@AutoConfigureAfter(value = {RabbitAutoConfiguration.class, MicrometerAutoConfiguration.class,},
    name = {"com.binance.platform.rabbit.RabbitAutoConfiguration"})
@Configuration
public class JmsAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(JmsAutoConfiguration.class);

    // kafka客户端监控
    @Configuration
    @ConditionalOnClass(KafkaClient.class)
    public static class KafkaClientMetricsAutoConfiguration {
        @Bean
        public KafkaMetricsBinder getKafkaMetricsBinder() {
            return new KafkaMetricsBinder();
        }
    }

    @Configuration
    @ConditionalOnClass(CachingConnectionFactory.class)
    public static class RabbitMetricsAutoConfiguration {
        @Bean
        @ConditionalOnBean(CachingConnectionFactory.class)
        public RabbitMetricsBinder rabbitCustomizer(CachingConnectionFactory connectionFactory,
            CustomizerTag platformTag) {
            return new RabbitMetricsBinder(connectionFactory, platformTag.getTags());
        }

    }
}
