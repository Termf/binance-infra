package com.binance.platform.monitor.metric.jms;

import com.sun.jmx.interceptor.DefaultMBeanServerInterceptor;
import com.sun.jmx.mbeanserver.JmxMBeanServer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.QueryExp;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/9/7
 */
public class KafkaMetricsBinder implements ApplicationContextAware, MeterBinder, ApplicationListener<KafkaMetricsBinder.DoKafkaMetricsBindEvent> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMetricsBinder.class);
    public static final String TAG_CLIENT_ID = "client-id";
    public static final String TAG_NAME_BEAN_NAME = "beanName";
    private static final AtomicInteger KAFKA_LISTENER_ID_SEQUENCE = new AtomicInteger(1);
    private static final AtomicInteger PRIVATE_PRODUCER_ID_SEQUENCE = new AtomicInteger(1);
    private static final AtomicInteger PRIVATE_CONSUMER_ID_SEQUENCE = new AtomicInteger(1);
    private AtomicBoolean hasRun = new AtomicBoolean(false);
    private Map<String, KafkaMetrics> clientIdMetricsMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    private MeterRegistry registry;

    public KafkaMetricsBinder() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PreDestroy
    public void destroy() {
        log.info("close all kafka KafkaMetrics");
        clientIdMetricsMap.entrySet().forEach((entry) -> {
            KafkaMetrics kafkaMetrics = entry.getValue();
            kafkaMetrics.close();
        });
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
    }

    private <T> void bindSpringManagedBean(Class<T> classT,
                                           Function<T, Map<MetricName, ? extends Metric>> metricSupplier) {
        try {
            Map<String, T> clientMap = applicationContext.getBeansOfType(classT);
            if (!clientMap.isEmpty()) {
                clientMap.forEach((beanName, client) -> {
                    ArrayList<Tag> tags = new ArrayList<>();
                    tags.add(Tag.of(TAG_NAME_BEAN_NAME, beanName));

                    Map<MetricName, ? extends Metric> metricsMap = metricSupplier.apply(client);
                    String clientId = getClientId(metricsMap);
                    registerKafkaMetrics(beanName, clientId, tags, metricsMap);
                });
            }
        } catch (Exception e) {
            log.warn("add kafka metrics failed.", e);
        }
    }

    public void registerKafkaMetrics(String beanName, String clientId,
                                      ArrayList<Tag> tags, Map<MetricName, ? extends Metric> metricsMap) {

        if (!StringUtils.hasText(clientId)) {
            log.warn(TAG_CLIENT_ID + " not found. beanName:[{}]", beanName);
        }
        if (!clientIdMetricsMap.keySet().contains(clientId)) {
            KafkaMetrics clientMetrics = new KafkaMetrics(() -> metricsMap, tags);
            clientIdMetricsMap.put(clientId, clientMetrics);
            clientMetrics.bindTo(registry);
            log.info("kafka metrics registered, beanName:[{}], clientId:[{}]", beanName, clientId);
        } else {
            log.info("kafka metrics duplicated, beanName:[{}], clientId:[{}], ignored", beanName, clientId);
        }
    }

    private String getClientId(Map<MetricName, ? extends Metric> metrics) {
        Iterator<MetricName> keySetIterator = metrics.keySet().iterator();
        if (keySetIterator.hasNext()) {
            return keySetIterator.next().tags().get(TAG_CLIENT_ID);
        }

        return null;
    }

    private void bindPrivateClients() {
        // producer
        Map<String, Map<MetricName, ? extends Metric>> producerMetricsMap = resolveKafkaMetrics("kafka.producer:type=*,client-id=*");
        producerMetricsMap.forEach((clientId, metricsMap) -> {
            try {
                ArrayList<Tag> tags = new ArrayList<>();
                String clientName = "private-producer-" + PRIVATE_PRODUCER_ID_SEQUENCE.getAndIncrement();
                tags.add(Tag.of(TAG_NAME_BEAN_NAME, clientName));

                registerKafkaMetrics(clientName, clientId, tags, metricsMap);
            } catch (Exception e) {
                log.error("add kafka producer metrics error. clientId=" + clientId, e);
            }
        });

        // consumer
        Map<String, Map<MetricName, ? extends Metric>> consumerMetricsMap = resolveKafkaMetrics("kafka.consumer:type=*,client-id=*");
        consumerMetricsMap.forEach((clientId, metricsMap) -> {
            try {
                ArrayList<Tag> tags = new ArrayList<>();
                String clientName = "private-consumer-" + PRIVATE_CONSUMER_ID_SEQUENCE.getAndIncrement();
                tags.add(Tag.of(TAG_NAME_BEAN_NAME, clientName));

                registerKafkaMetrics(clientName, clientId, tags, metricsMap);
            } catch (Exception e) {
                log.error("add kafka consumer metrics error. clientId=" + clientId, e);
            }
        });
    }

    private static Map<String, Map<MetricName, ? extends Metric>> resolveKafkaMetrics(String queryQname) {
        Map<String, Map<MetricName, ? extends Metric>> clientMetricsMap = new HashMap<>();
        try {
            MBeanServer mBeanServer = getMBeanServer();
            Set<ObjectName> kafkaProducerObjectNames = mBeanServer.queryNames(new ObjectName(queryQname), new QueryExp() {
                @Override
                public boolean apply(ObjectName name) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
                    String type = name.getKeyProperty("type");
                    if (type != null && !"app-info".equals(type) && !type.endsWith("count")) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void setMBeanServer(MBeanServer s) {
                }
            });

            kafkaProducerObjectNames.forEach(objectName -> {
                try {
                    String clientId = objectName.getKeyProperty("client-id");
                    if (mBeanServer instanceof JmxMBeanServer) {
                        JmxMBeanServer jmxMBeanServer = (JmxMBeanServer) mBeanServer;

                        Field mbsInterceptorField = JmxMBeanServer.class.getDeclaredField("mbsInterceptor");
                        ReflectionUtils.makeAccessible(mbsInterceptorField);
                        DefaultMBeanServerInterceptor mbsInterceptor = (DefaultMBeanServerInterceptor) mbsInterceptorField.get(jmxMBeanServer);
                        Method getMBeanMethod = DefaultMBeanServerInterceptor.class.getDeclaredMethod("getMBean", ObjectName.class);
                        ReflectionUtils.makeAccessible(getMBeanMethod);
                        Object kafkaMbean = getMBeanMethod.invoke(mbsInterceptor, objectName);
                        Class<?> kafkaMbeanClass = kafkaMbean.getClass();
                        Field metricsField = kafkaMbeanClass.getDeclaredField("metrics");
                        ReflectionUtils.makeAccessible(metricsField);
                        Map<String, KafkaMetric> kafkaMetrics = (Map<String, KafkaMetric>) metricsField.get(kafkaMbean);
                        Map<MetricName, KafkaMetric> singleMetricMap = new HashMap<>();
                        kafkaMetrics.values().forEach((metricItem -> {
                            singleMetricMap.put(metricItem.metricName(), metricItem);
                        }));
                        clientMetricsMap.putIfAbsent(clientId, singleMetricMap);
                    }
                } catch (Exception e) {
                    log.error("resolve metrics from Mbean error. qname=" + objectName.toString(), e);
                }
            });
        } catch (Exception e) {
            log.error("search kafka metrics mbean error. queryQname=" + queryQname, e);
        }

        return clientMetricsMap;
    }

    public static MBeanServer getMBeanServer() {
        List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (!mBeanServers.isEmpty()) {
            return mBeanServers.get(0);
        }
        return ManagementFactory.getPlatformMBeanServer();
    }

    private void doBind() {
        ClassLoader classLoader = getClass().getClassLoader();
        if (!ClassUtils.isPresent("org.apache.kafka.clients.KafkaClient", classLoader)) {
            return;
        }
        try {
            this.registry = applicationContext.getBean(MeterRegistry.class);
        } catch (Exception e) {
            log.warn("retrieve MeterRegistry bean error", e);
            return;
        }
        // Producer
        bindSpringManagedBean(Producer.class, (producer) -> producer.metrics());
        // Consumer
        bindSpringManagedBean(Consumer.class, (consumer) -> consumer.metrics());
        // KafkaTemplate
        if (ClassUtils.isPresent("org.springframework.kafka.core.KafkaOperations", classLoader)) {
            SpringKafkaMetrics.bindMetrics(this);
        }
        // KafkaListener
        if (ClassUtils.isPresent("org.springframework.kafka.config.KafkaListenerEndpointRegistry", classLoader)) {
            SpringKafkaMetrics.bindKafkaListenerEndpointRegistry(this);
        }

        // other private clients
        // 这个要放在最后执行，前面其他组件已经注册过的客户端在这里会被忽略钓（根据client-id）
        bindPrivateClients();
    }

    @Override
    public void onApplicationEvent(DoKafkaMetricsBindEvent event) {
        try {
            Thread kafkaMetricBinderThread = new Thread(() -> {
                try {
                    doBind();
                } catch (Throwable throwable) {
                    log.warn("add kafka metrics error", throwable);
                }
            });
            kafkaMetricBinderThread.setName("kafka-metrics-binder");
            kafkaMetricBinderThread.setDaemon(true);
            kafkaMetricBinderThread.start();
        } catch (Throwable throwable) {
            log.warn("Kafka metrics error", throwable);
        }
    }

    public static class SpringKafkaMetrics {
        public static void bindMetrics(KafkaMetricsBinder kafkaMetricsBinder) {
            kafkaMetricsBinder.bindSpringManagedBean(KafkaOperations.class, (kafkaOperations) -> kafkaOperations.metrics());
        }

        public static void bindKafkaListenerEndpointRegistry(KafkaMetricsBinder kafkaMetricsBinder) {
            try {
                Map<String, KafkaListenerEndpointRegistry> registryMap = kafkaMetricsBinder.applicationContext.getBeansOfType(KafkaListenerEndpointRegistry.class);
                registryMap.forEach((beanName, endpointRegistry) -> {
                    Collection<MessageListenerContainer> listenerContainers = endpointRegistry.getAllListenerContainers();
                    for (MessageListenerContainer listenerContainer : listenerContainers) {
                        Iterator<Map.Entry<String, Map<MetricName, ? extends Metric>>> clientIdMetricsIterator =
                                listenerContainer.metrics().entrySet().iterator();
                        if (clientIdMetricsIterator.hasNext()) {
                            Map.Entry<String, Map<MetricName, ? extends Metric>> clientIdMetricsEntry = clientIdMetricsIterator.next();
                            String clientId = clientIdMetricsEntry.getKey();
                            Map<MetricName, ? extends Metric> metricsMap = clientIdMetricsEntry.getValue();

                            ArrayList<Tag> tags = new ArrayList<>();
                            String clientName = "KafkaListenerClient-" + KAFKA_LISTENER_ID_SEQUENCE.getAndIncrement();
                            tags.add(Tag.of(TAG_NAME_BEAN_NAME, clientName));

                            kafkaMetricsBinder.registerKafkaMetrics(clientName, clientId, tags, metricsMap);
                        }
                    }
                });
            } catch (Exception e) {
                log.warn("add kafka metrics for KafkaListener failed", e);
            }
        }
    }

    public static class DoKafkaMetricsBindEvent extends SpringApplicationEvent {
        private ConfigurableApplicationContext context;

        public DoKafkaMetricsBindEvent(SpringApplication application, String[] args, ConfigurableApplicationContext context) {
            super(application, args);
            this.context = context;
        }

        public ConfigurableApplicationContext getApplicationContext() {
            return context;
        }
    }
}
