package com.binance.platform.monitor.model;

import lombok.AllArgsConstructor;

public class SystemStatsEnum {

    @AllArgsConstructor
    public enum Network {
        KBYTES_RECEIVED("KBYTES_RECEIVED", "system.network.kbytes.received"),
        KBYTES_SENT("KBYTES_SENT", "system.network.kbytes.sent"),
        PACKETS_RECEIVED("PACKETS_RECEIVED", "system.network.packets.received"),
        PACKETS_SENT("PACKETS_SENT", "system.network.packets.sent");

        private String code;
        private String meterName;

        public String getCode() {
            return code;
        }

        public String getMeterName() {
            return meterName;
        }
    }

    @AllArgsConstructor
    public enum Tcp {
        ESTABLISHED("ESTABLISHED"),
        SYNSENT("SYNSENT"),
        SYNRECV("SYNRECV"),
        FINWAIT1("FINWAIT1"),
        FINWAIT2("FINWAIT2"),
        TIMEWAIT("TIMEWAIT"),
        CLOSE("CLOSE"),
        CLOSEWAIT("CLOSEWAIT"),
        LASTACK("LASTACK"),
        LISTEN("LISTEN"),
        CLOSING("CLOSING");

        private String code;

        public String getCode() {
            return code;
        }

    }

    @AllArgsConstructor
    public enum SystemTcpQueue {
        SYSTEM_RECEIVE_QUEUE_CONNECTIONS("SYSTEM_RECEIVE_QUEUE_CONNECTIONS", "system.network.receive.queue.connections"),
        SYSTEM_RECEIVE_QUEUE_KBYTES("SYSTEM_RECEIVE_QUEUE_KBYTES", "system.network.receive.queue.kbytes"),
        SYSTEM_SEND_QUEUE_CONNECTIONS("SYSTEM_SEND_QUEUE_CONNECTIONS", "system.network.send.queue.connections"),
        SYSTEM_SEND_QUEUE_KBYTES("SYSTEM_SEND_QUEUE_KBYTES", "system.network.send.queue.kbytes"),
        SYSTEM_TCP_REQ_FULL_DO_COOKIES("SYSTEM_TCP_REQ_FULL_DO_COOKIES", "system.network.tcpReqQFullDoCookies"),
        SYSTEM_SYN_COOKIES_SENT("SYSTEM_SYN_COOKIES_SENT", "system.network.syncookiesSent"),
        SYSTEM_LISTEN_OVER_FLOWS("SYSTEM_LISTEN_OVER_FLOWS", "system.network.listenOverflows"),
        SYSTEM_LISTEN_DROPS("SYSTEM_LISTEN_DROPS", "system.network.listenDrops"),
        SYSTEM_TW_RECYCLED("SYSTEM_TW_RECYCLED", "system.network.twRecycled");

        private String code;
        private String meterName;

        public String getCode() {
            return code;
        }

        public String getMeterName() {
            return meterName;
        }
    }
}
