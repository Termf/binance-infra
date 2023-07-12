package com.binance.platform.monitor.metric.system;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.binance.master.utils.StringUtils;
import com.binance.platform.monitor.model.SystemStatsEnum;
import com.binance.platform.monitor.utils.FileUtil;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class NetworkMetrics implements MeterBinder {

    private final Iterable<Tag> tags;

    private final NetworkStats networkStats = new NetworkStats();

    private static final int KILOBYTE = 1024;

    // cash refresh time
    public static final int CACHE_REFRESH_TIME = 1000;

    public NetworkMetrics() {
        this(Collections.emptyList());
    }

    public NetworkMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    public void bindTo(MeterRegistry meterRegistry) {
        try {
            FunctionCounter
                .builder(SystemStatsEnum.Network.KBYTES_RECEIVED.getMeterName(),
                    SystemStatsEnum.Network.KBYTES_RECEIVED, this::getNetworkMetric)
                .tags(tags).baseUnit("kbytes").register(meterRegistry);
            FunctionCounter.builder(SystemStatsEnum.Network.KBYTES_SENT.getMeterName(),
                SystemStatsEnum.Network.KBYTES_SENT, this::getNetworkMetric).tags(tags).baseUnit("kbytes")
                .register(meterRegistry);
            FunctionCounter
                .builder(SystemStatsEnum.Network.PACKETS_RECEIVED.getMeterName(),
                    SystemStatsEnum.Network.PACKETS_RECEIVED, this::getNetworkMetric)
                .tags(tags).register(meterRegistry);
            FunctionCounter.builder(SystemStatsEnum.Network.PACKETS_SENT.getMeterName(),
                SystemStatsEnum.Network.PACKETS_SENT, this::getNetworkMetric).tags(tags).register(meterRegistry);
        } catch (Throwable e) {
            // igore
        }
    }

    private Double getNetworkMetric(SystemStatsEnum.Network type) {
        // refresh data from system
        networkStats.refreshData();

        switch (type) {
            case KBYTES_SENT:
                return new BigDecimal(((double)networkStats.bytesSent) / KILOBYTE).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            case PACKETS_SENT:
                return (double)networkStats.packetsSent;
            case KBYTES_RECEIVED:
                return new BigDecimal(((double)(double)networkStats.bytesReceived) / KILOBYTE)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            case PACKETS_RECEIVED:
                return (double)networkStats.packetsReceived;
            default:
                return 0.0;
        }
    }

    private static class NetworkStats {
        public volatile long bytesReceived = 0;
        public volatile long bytesSent = 0;
        public volatile long packetsReceived = 0;
        public volatile long packetsSent = 0;

        private long lastRefresh = 0;

        private final Lock lock = new ReentrantLock();

        private final String[] ignoredDevicePrefixes = new String[] {"lo", "veth", "docker"};

        /**
         * refresh data from system
         */
        public void refreshData() {
            if (lock.tryLock()) {
                try {
                    doRefresh();
                } catch (Exception ex) {

                } finally {
                    lock.unlock();
                }
            }
        }

        private void doRefresh() {
            if (System.currentTimeMillis() - lastRefresh < CACHE_REFRESH_TIME) {
                // use cache
                return;
            }
            bytesReceived = 0;
            bytesSent = 0;
            packetsReceived = 0;
            packetsSent = 0;

            List<String> netInfoList = FileUtil.readFile("/proc/net/dev");

            // start from interfaces, ignore net headr info
            for (int i = 2; i < netInfoList.size(); i++) {
                String netInfo = netInfoList.get(i).trim();
                String[] netInfoArray = netInfo.split("\\s+");
                if (netInfoArray.length == 17) {
                    if (isIgnoredDevice(netInfoArray[0])) {
                        continue;
                    }
                    bytesReceived += Long.parseLong(netInfoArray[1]);
                    packetsReceived += Long.parseLong(netInfoArray[2]);
                    bytesSent += Long.parseLong(netInfoArray[9]);
                    packetsSent += Long.parseLong(netInfoArray[10]);
                }
            }
            this.lastRefresh = System.currentTimeMillis();
        }

        /**
         * judge whether some devices should be ignore or not
         * 
         * @param deviceName
         * @return
         */
        private boolean isIgnoredDevice(String deviceName) {
            for (String prefix : ignoredDevicePrefixes) {
                if (StringUtils.startsWith(deviceName, prefix)) {
                    return true;
                }
            }
            return false;
        }
    }
}
