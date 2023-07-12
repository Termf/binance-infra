package com.binance.platform.monitor.metric.system;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.binance.master.utils.StringUtils;
import com.binance.platform.monitor.model.SystemStatsEnum;
import com.binance.platform.monitor.utils.FileUtil;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class TcpUdpMetrics implements MeterBinder {

    private static final Logger log = LoggerFactory.getLogger(TcpUdpMetrics.class);

    private final Iterable<Tag> tags;
    private Environment env;
    private static final Map<String, SystemStatsEnum.Tcp> TCP_STATE_MAPPING = new HashMap<String, SystemStatsEnum.Tcp>() {
        private static final long serialVersionUID = 1L;
        {
            put("01", SystemStatsEnum.Tcp.ESTABLISHED);
            put("02", SystemStatsEnum.Tcp.SYNSENT);
            put("03", SystemStatsEnum.Tcp.SYNRECV);
            put("04", SystemStatsEnum.Tcp.FINWAIT1);
            put("05", SystemStatsEnum.Tcp.FINWAIT2);
            put("06", SystemStatsEnum.Tcp.TIMEWAIT);
            put("07", SystemStatsEnum.Tcp.CLOSE);
            put("08", SystemStatsEnum.Tcp.CLOSEWAIT);
            put("09", SystemStatsEnum.Tcp.LASTACK);
            put("0A", SystemStatsEnum.Tcp.LISTEN);
            put("0B", SystemStatsEnum.Tcp.CLOSING);
        }
    };

    private final TcpStats tcpStats = new TcpStats();

    // cash refresh time
    public static final int CACHE_REFRESH_TIME = 1000;

    private static final int KILOBYTE = 1024;

    private static String serverPortHex;

    public TcpUdpMetrics(Environment env, Iterable<Tag> tags) {
        this.tags = tags;
        this.serverPortHex =
                Optional.ofNullable(env.getProperty("server.port")).map(value -> Integer.toHexString(Integer.parseInt(value))).orElse("");;
        log.info("serverPortHex:{}", serverPortHex);
    }

    public void bindTo(MeterRegistry meterRegistry) {
        try {

            // register tcp meter
            Arrays.asList(SystemStatsEnum.Tcp.values()).stream().forEach(item -> {
                Gauge.builder("system.tcp.stats", () -> getTcpMetric(item, false)).tags(tags).tags("stat", item.getCode()).register(meterRegistry);
            });

            // register tcp6 meter
            Arrays.asList(SystemStatsEnum.Tcp.values()).stream().forEach(item -> {
                Gauge.builder("system.tcp6.stats", () -> getTcpMetric(item, true)).tags(tags).tag("stat", item.getCode()).register(meterRegistry);
            });

            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_RECEIVE_QUEUE_CONNECTIONS))
                    .tags(tags).tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_RECEIVE_QUEUE_CONNECTIONS.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_RECEIVE_QUEUE_KBYTES))
                    .tags(tags).tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_RECEIVE_QUEUE_KBYTES.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_SEND_QUEUE_CONNECTIONS))
                    .tags(tags).tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_SEND_QUEUE_CONNECTIONS.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_SEND_QUEUE_KBYTES)).tags(tags)
                    .tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_SEND_QUEUE_KBYTES.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_TCP_REQ_FULL_DO_COOKIES))
                    .tags(tags).tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_TCP_REQ_FULL_DO_COOKIES.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_SYN_COOKIES_SENT)).tags(tags)
                    .tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_SYN_COOKIES_SENT.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_LISTEN_OVER_FLOWS)).tags(tags)
                    .tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_LISTEN_OVER_FLOWS.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_LISTEN_DROPS)).tags(tags)
                    .tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_LISTEN_DROPS.getMeterName()).register(meterRegistry);
            Gauge.builder("system.tcpqueue.stats", () -> getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue.SYSTEM_TW_RECYCLED)).tags(tags)
                    .tag("stat", SystemStatsEnum.SystemTcpQueue.SYSTEM_TW_RECYCLED.getMeterName()).register(meterRegistry);
        } catch (Throwable e) {
            // igore
        }

    }

    /**
     * get tcp or tcp 6 meter
     *
     * @param type
     * @param isTcp6
     * @return
     */
    private Long getTcpMetric(SystemStatsEnum.Tcp type, boolean isTcp6) {
        // refresh data from system
        tcpStats.refreshData();

        if (isTcp6) {
            return tcpStats.tcp6Map.getOrDefault(type, 0l);
        } else {
            return tcpStats.tcpMap.getOrDefault(type, 0l);
        }
    }

    /**
     * get tomcat tcp queue metric
     *
     * @param type
     * @return
     */
    private Double getTomcatTcpQueueMetric(SystemStatsEnum.SystemTcpQueue type) {
        // refresh data from system
        tcpStats.refreshData();

        switch (type) {
            case SYSTEM_RECEIVE_QUEUE_KBYTES:
                return new BigDecimal(((double) tcpStats.receiveQueueBytes) / KILOBYTE).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            case SYSTEM_RECEIVE_QUEUE_CONNECTIONS:
                return (double) tcpStats.receiveQueueWaits;
            case SYSTEM_SEND_QUEUE_KBYTES:
                return new BigDecimal(((double) (double) tcpStats.sendQueueBytes) / KILOBYTE).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            case SYSTEM_SEND_QUEUE_CONNECTIONS:
                return (double) tcpStats.sendQueueWaits;
            case SYSTEM_TCP_REQ_FULL_DO_COOKIES:
                return (double) tcpStats.tcpReqQFullDoCookies;
            case SYSTEM_SYN_COOKIES_SENT:
                return (double) tcpStats.syncookiesSent;
            case SYSTEM_LISTEN_OVER_FLOWS:
                return (double) tcpStats.listenOverflows;
            case SYSTEM_LISTEN_DROPS:
                return (double) tcpStats.listenDrops;
            case SYSTEM_TW_RECYCLED:
                return (double) tcpStats.twRecycled;
            default:
                return 0.0;
        }
    }

    private static class TcpStats {
        public Map<SystemStatsEnum.Tcp, Long> tcpMap = new ConcurrentHashMap<SystemStatsEnum.Tcp, Long>();
        public Map<SystemStatsEnum.Tcp, Long> tcp6Map = new ConcurrentHashMap<SystemStatsEnum.Tcp, Long>();
        public volatile long receiveQueueWaits = 0;
        public volatile long receiveQueueBytes = 0;
        public volatile long sendQueueWaits = 0;
        public volatile long sendQueueBytes = 0;
        public volatile long tcpReqQFullDoCookies = 0;
        public volatile long syncookiesSent = 0;
        public volatile long listenOverflows = 0;
        public volatile long listenDrops = 0;
        public volatile long twRecycled = 0;


        private long lastRefresh = 0;

        private final Lock lock = new ReentrantLock();

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

            // refresh map
            tcpMap.clear();
            tcp6Map.clear();
            receiveQueueWaits = 0;
            receiveQueueBytes = 0;
            sendQueueWaits = 0;
            sendQueueBytes = 0;
            tcpReqQFullDoCookies = 0;
            syncookiesSent = 0;
            listenOverflows = 0;
            listenDrops = 0;
            twRecycled = 0;

            // set tcp status
            setTcpStatus(tcpMap, "/proc/net/tcp");

            // set tcp6 status
            setTcpStatus(tcp6Map, "/proc/net/tcp6");

            try {
                // set netstat status
                setNetstatStatus("/proc/" + getProcessID() + "/net/netstat");
            } catch (Exception ex) {
                log.error("setNetstatStatus error: ",ex);
            }

            this.lastRefresh = System.currentTimeMillis();
        }

        private void setNetstatStatus(String filePath) {
            List<String> netstatInfoList = FileUtil.readFile(filePath);
            if (netstatInfoList.size() >= 2) {
                String tcpExtInfo = netstatInfoList.get(1);
                String[] tcpExtInfoArray = tcpExtInfo.split("\\s+");
                tcpReqQFullDoCookies += Long.parseLong(tcpExtInfoArray[75]);
                syncookiesSent += Long.parseLong(tcpExtInfoArray[1]);
                listenOverflows += Long.parseLong(tcpExtInfoArray[19]);
                listenDrops += Long.parseLong(tcpExtInfoArray[20]);
                twRecycled += Long.parseLong(tcpExtInfoArray[12]);
            }
        }

        private void setTcpStatus(Map<SystemStatsEnum.Tcp, Long> tcpMap, String filePath) {
            List<String> tcpInfoList = FileUtil.readFile(filePath);
            // ignore header, start from 1
            for (int i = 1; i < tcpInfoList.size(); i++) {
                String tcpInfo = tcpInfoList.get(i).trim();
                String[] tcpInfoArray = tcpInfo.split("\\s+");
                // get tcp state such as :01,02,0A,0B...
                String tcpState = tcpInfoArray[3];
                SystemStatsEnum.Tcp tcpEnum = TCP_STATE_MAPPING.get(tcpState);
                if (tcpEnum != null) {
                    tcpMap.put(tcpEnum, tcpMap.getOrDefault(tcpEnum, 0l) + 1);
                }

                // calculate tomcat send queue, receive queue info
                if (tcpEnum == SystemStatsEnum.Tcp.ESTABLISHED && StringUtils.equalsIgnoreCase(tcpInfoArray[1].split(":")[1], serverPortHex)) {
                    String[] queueInfoArray = tcpInfoArray[4].split(":");
                    int txQueue = new BigInteger(queueInfoArray[0], 16).intValue();
                    int rxQueue = new BigInteger(queueInfoArray[1], 16).intValue();
                    if (txQueue != 0) {
                        sendQueueWaits++;
                        sendQueueBytes += txQueue;
                    }

                    if (rxQueue != 0) {
                        receiveQueueWaits++;
                        receiveQueueBytes += rxQueue;
                    }
                }
            }
        }

        private final int getProcessID() {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
        }
    }
}
