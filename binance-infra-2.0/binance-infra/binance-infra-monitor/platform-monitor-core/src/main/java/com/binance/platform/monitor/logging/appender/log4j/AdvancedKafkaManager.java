
package com.binance.platform.monitor.logging.appender.log4j;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.binance.master.log.layout.MaskUtil;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.logging.appender.DefaultKafkaProducerFactory;
import com.binance.platform.monitor.logging.appender.KafkaProducerFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class AdvancedKafkaManager extends AbstractManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedKafkaManager.class);

    private static final ExecutorService SEND_MESSAGE_EXECUTOR =
        new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(500),
            new ThreadFactoryBuilder().setNameFormat("kafka-sendlog-pool-%d").build(),
            new ThreadPoolExecutor.DiscardPolicy());

    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
    public static final String MANAGEMENT_LOGGING_MASK_ENABLE = "management.logging.mask.enable";
    private static final KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();
    private final Properties config = new Properties();
    private Producer<byte[], byte[]> producer;

    public AdvancedKafkaManager(final LoggerContext loggerContext, final String name, final String bootstrapServers) {
        super(loggerContext, name);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "log-appender-1");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.ACKS_CONFIG, "0");
        config.put(ProducerConfig.RETRIES_CONFIG, 0);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = pmrpr.getResources("classpath*:log/log-mask-pattern.txt");
            MaskUtil.init(resources);
        } catch (Exception e) {
            LOGGER.warn("cannot load or parse the log-mask-pattern.txt file!");
        }
    }

    private void closeProducer(final long timeout, final TimeUnit timeUnit) {
        if (producer != null) {
            final Thread closeThread = new Log4jThread(new Runnable() {
                @Override
                public void run() {
                    if (producer != null) {
                        producer.close();
                    }
                }
            }, "AdvancedKafkaManager-CloseThread");
            closeThread.setDaemon(true);
            closeThread.start();
            try {
                closeThread.join(timeUnit.toMillis(timeout));
            } catch (final InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0) {
            closeProducer(timeout, timeUnit);
        }
        return true;
    }

    public void send(final String topic, final String text)
        throws ExecutionException, InterruptedException, TimeoutException {
        SEND_MESSAGE_EXECUTOR.submit(new Runnable() {

            @Override
            public void run() {
                if (producer != null) {
                    try {
                        String enable = EnvUtil.getProperty(MANAGEMENT_LOGGING_MASK_ENABLE, "false");
                        if (BooleanUtils.toBoolean(enable)) {
                            StringBuilder sb = MaskUtil.maskSensitiveInfo(text);
                            final ProducerRecord<byte[], byte[]> newRecord =
                                new ProducerRecord<>(topic, sb.toString().getBytes());
                            producer.send(newRecord);
                        } else {
                            final ProducerRecord<byte[], byte[]> newRecord =
                                new ProducerRecord<>(topic, text.getBytes());
                            producer.send(newRecord);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    public void startup() {
        producer = producerFactory.newKafkaProducer(config);
    }

}
