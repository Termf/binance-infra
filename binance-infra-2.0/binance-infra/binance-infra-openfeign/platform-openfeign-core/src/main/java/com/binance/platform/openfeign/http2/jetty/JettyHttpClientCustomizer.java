package com.binance.platform.openfeign.http2.jetty;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import org.eclipse.jetty.client.AbstractConnectionPool;
import org.eclipse.jetty.client.ConnectionPool;
import org.eclipse.jetty.client.DuplexConnectionPool;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpDestination;
import org.eclipse.jetty.client.MultiplexConnectionPool;
import org.eclipse.jetty.client.Origin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * binanceframework
 *
 * @author Thomas Li
 * Date: 2021/4/9
 */
public class JettyHttpClientCustomizer {
    private static final Logger log = LoggerFactory.getLogger(JettyHttpClientCustomizer.class);
    private static final String METRIC_NAME = "jetty.http.client.connection.pool";

    private Map<Origin, List<GaugeHolder>> metricHolderMap = new ConcurrentHashMap<>();

    public static ConcurrentMap<Origin, HttpDestination> customize(HttpClient httpClient) {
        try {
            Field destinationsField = HttpClient.class.getDeclaredField("destinations");
            boolean assignableFrom = destinationsField.getType().isAssignableFrom(ConcurrentMap.class);
            if (assignableFrom) {
                ReflectionUtils.makeAccessible(destinationsField);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                ReflectionUtils.makeAccessible(modifiersField);
                modifiersField.setInt(destinationsField, destinationsField.getModifiers() & ~Modifier.FINAL);

                JettyDestinationsConcurrentMap watchedMap = new JettyDestinationsConcurrentMap((ConcurrentMap<Origin, HttpDestination>) destinationsField.get(httpClient));
                destinationsField.set(httpClient, watchedMap);
                return watchedMap;
            }
        } catch (Exception e) {
            log.warn("add jetty connection pool metrics error", e);
        }

        return null;
    }

    private void clear() {
        Set<Origin> origins = metricHolderMap.keySet();
        for (Origin origin : origins) {
            destinationRemoved(origin);
        }
    }

    private void destinationRemoved(Object origin) {
        try {
            List<GaugeHolder> gaugeHolderList = metricHolderMap.get(origin);
            if (gaugeHolderList != null) {
                for (GaugeHolder gaugeHolder : gaugeHolderList) {
                    Metrics.globalRegistry.remove(gaugeHolder.gauge);
                }
            }
        } catch (Exception e) {
            log.warn("jetty http client metrics remove error - {}", e.getMessage());
        }
    }

    private void destinationAdded(HttpDestination httpDestination) {
        if (metricHolderMap.size() > 1000) {
            return;
        }

        try {
            doDestinationAdd(httpDestination);
        } catch (Exception e) {
            log.warn("jetty http client metrics add error - {}", e.getMessage());
        }
    }

    private void doDestinationAdd(HttpDestination httpDestination) {
        String host = httpDestination.getHost();
        int port = httpDestination.getPort();
        List<Tag> tagList = new ArrayList<>();
        tagList.add(Tag.of("host", host));
        tagList.add(Tag.of("port", String.valueOf(port)));

        ConnectionPool connectionPool = httpDestination.getConnectionPool();

        if (connectionPool instanceof DuplexConnectionPool) {
            // used to http1.1
        } else if (connectionPool instanceof MultiplexConnectionPool) {
            // used to http2.0
            String poolType = "MultiplexConnectionPool";
            ArrayList<GaugeHolder> gaugeList = new ArrayList<>();
            metricHolderMap.put(httpDestination.getOrigin(), gaugeList);

            commonConnectionPoolMetrics(httpDestination, tagList, poolType, gaugeList);

            addGauge(tagList, poolType, "maxMultiplex",
                    () -> ((MultiplexConnectionPool) httpDestination.getConnectionPool()).getMaxMultiplex(), gaugeList);
        }
    }

    private void commonConnectionPoolMetrics(HttpDestination httpDestination, List<Tag> tagList, String poolType, ArrayList<GaugeHolder> gaugeList) {
        addGauge(tagList, poolType, "maxConnectionCount",
                () -> ((AbstractConnectionPool) httpDestination.getConnectionPool()).getMaxConnectionCount(), gaugeList);

        addGauge(tagList, poolType, "connectionCount",
                () -> ((AbstractConnectionPool) httpDestination.getConnectionPool()).getConnectionCount(), gaugeList);

        addGauge(tagList, poolType, "activeConnectionCount",
                () -> ((AbstractConnectionPool) httpDestination.getConnectionPool()).getActiveConnectionCount(), gaugeList);

        addGauge(tagList, poolType, "idleConnectionCount",
                () -> ((AbstractConnectionPool) httpDestination.getConnectionPool()).getIdleConnectionCount(), gaugeList);

        addGauge(tagList, poolType, "pendingConnectionCount",
                () -> ((AbstractConnectionPool) httpDestination.getConnectionPool()).getPendingConnectionCount(), gaugeList);
    }

    private Gauge addGauge(List<Tag> tagList, String poolType, String gaugeType, Supplier<Integer> metricSupplier, List<GaugeHolder> gaugeList) {
        MetricNumber metricNumber = new MetricNumber(metricSupplier);
        Gauge gauge = Gauge.builder(METRIC_NAME, metricNumber, Number::doubleValue)
                .tags(tagList)
                .tag("poolType", poolType)
                .tag("gaugeType", gaugeType)
                .register(Metrics.globalRegistry);
        gaugeList.add(new GaugeHolder(metricNumber, gauge));
        return gauge;
    }

    private static class GaugeHolder {
        private Number value;
        private Gauge gauge;

        public GaugeHolder(Number value, Gauge gauge) {
            this.value = value;
            this.gauge = gauge;
        }
    }

    private static class MetricNumber extends Number {
        private final SoftReference<Supplier<Integer>> supplierWeakReference;

        public MetricNumber(Supplier<Integer> supplier) {
            this.supplierWeakReference = new SoftReference<>(supplier);
        }

        @Override
        public int intValue() {
            Supplier<Integer> supplier = supplierWeakReference.get();
            if (supplier != null) {
                return supplier.get();
            }
            return 0;
        }

        @Override
        public long longValue() {
            return intValue();
        }

        @Override
        public float floatValue() {
            return (float)intValue();
        }

        @Override
        public double doubleValue() {
            return intValue();
        }
    }

    private static class JettyDestinationsConcurrentMap implements ConcurrentMap<Origin, HttpDestination> {
        private final JettyHttpClientCustomizer jettyHttpClientCustomizer = new JettyHttpClientCustomizer();
        private final ConcurrentMap<Origin, HttpDestination> target;

        public JettyDestinationsConcurrentMap(ConcurrentMap<Origin, HttpDestination> target) {
            this.target = target;
        }

        @Override
        public HttpDestination putIfAbsent(Origin key, HttpDestination value) {
            HttpDestination existing = target.putIfAbsent(key, value);
            if (existing == null) {
                jettyHttpClientCustomizer.destinationAdded(value);
            }
            return existing;
        }

        @Override
        public HttpDestination computeIfAbsent(Origin key, Function<? super Origin, ? extends HttpDestination> mappingFunction) {
            Objects.requireNonNull(mappingFunction);
            HttpDestination v, newValue;
            return ((v = target.get(key)) == null &&
                    (newValue = mappingFunction.apply(key)) != null &&
                    (v = putIfAbsent(key, newValue)) == null) ? newValue : v;
        }

        @Override
        public boolean remove(Object key, Object value) {
            boolean removed = target.remove(key, value);
            if (removed) {
                jettyHttpClientCustomizer.destinationRemoved(key);
            }
            return removed;
        }

        @Override
        public HttpDestination remove(Object key) {
            jettyHttpClientCustomizer.destinationRemoved(key);
            return target.remove(key);
        }

        @Override
        public void clear() {
            target.clear();
            jettyHttpClientCustomizer.clear();
        }

        @Override
        public HttpDestination getOrDefault(Object key, HttpDestination defaultValue) {
            return target.getOrDefault(key, defaultValue);
        }

        @Override
        public void forEach(BiConsumer<? super Origin, ? super HttpDestination> action) {
            target.forEach(action);
        }

        @Override
        public boolean replace(Origin key, HttpDestination oldValue, HttpDestination newValue) {
            return target.replace(key, oldValue, newValue);
        }

        @Override
        public HttpDestination replace(Origin key, HttpDestination value) {
            return target.replace(key, value);
        }

        @Override
        public void replaceAll(BiFunction<? super Origin, ? super HttpDestination, ? extends HttpDestination> function) {
            target.replaceAll(function);
        }

        @Override
        public HttpDestination computeIfPresent(Origin key, BiFunction<? super Origin, ? super HttpDestination, ? extends HttpDestination> remappingFunction) {
            return target.computeIfPresent(key, remappingFunction);
        }

        @Override
        public HttpDestination compute(Origin key, BiFunction<? super Origin, ? super HttpDestination, ? extends HttpDestination> remappingFunction) {
            return target.compute(key, remappingFunction);
        }

        @Override
        public HttpDestination merge(Origin key, HttpDestination value, BiFunction<? super HttpDestination, ? super HttpDestination, ? extends HttpDestination> remappingFunction) {
            return target.merge(key, value, remappingFunction);
        }

        @Override
        public int size() {
            return target.size();
        }

        @Override
        public boolean isEmpty() {
            return target.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return target.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return target.containsValue(value);
        }

        @Override
        public HttpDestination get(Object key) {
            return target.get(key);
        }

        @Override
        public HttpDestination put(Origin key, HttpDestination value) {
            return target.put(key, value);
        }

        @Override
        public void putAll(Map<? extends Origin, ? extends HttpDestination> m) {
            target.putAll(m);
        }

        @Override
        public Set<Origin> keySet() {
            return target.keySet();
        }

        @Override
        public Collection<HttpDestination> values() {
            return target.values();
        }

        @Override
        public Set<Entry<Origin, HttpDestination>> entrySet() {
            return target.entrySet();
        }

        @Override
        public boolean equals(Object o) {
            return target.equals(o);
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }
    }
}
