
package com.binance.platform.monitor.logging.appender;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaProducerFactory {

    public static final Map<Properties, KafkaProducer<byte[], byte[]>> PRODUCER_CACHE =
        new ConcurrentHashMap<Properties, KafkaProducer<byte[], byte[]>>();

    Producer<byte[], byte[]> newKafkaProducer(Properties config);

    public static KafkaProducer<byte[], byte[]> getLogKafkaProducer() {
        if (!PRODUCER_CACHE.isEmpty()) {
            for (KafkaProducer<byte[], byte[]> value : PRODUCER_CACHE.values()) {
                return value;
            }
        }
        return null;
    }

}
