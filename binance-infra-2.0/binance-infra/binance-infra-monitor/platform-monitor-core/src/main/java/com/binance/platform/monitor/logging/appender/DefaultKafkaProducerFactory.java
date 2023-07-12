
package com.binance.platform.monitor.logging.appender;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

public class DefaultKafkaProducerFactory implements KafkaProducerFactory {

    @Override
    public org.apache.kafka.clients.producer.Producer<byte[], byte[]> newKafkaProducer(final Properties config) {
        if (PRODUCER_CACHE.containsKey(config)) {
            return PRODUCER_CACHE.get(config);
        } else {
            KafkaProducer<byte[], byte[]> kafkaProducer = new KafkaProducer<byte[], byte[]>(config);
            PRODUCER_CACHE.put(config, kafkaProducer);
            return kafkaProducer;
        }

    }

}
