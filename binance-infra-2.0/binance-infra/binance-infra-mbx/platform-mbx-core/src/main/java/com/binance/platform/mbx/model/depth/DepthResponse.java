package com.binance.platform.mbx.model.depth;

import com.binance.master.commons.ToString;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class DepthResponse extends ToString {

    private Long lastUpdateId;
    private final List<Info> bids = Lists.newArrayList();
    private final List<Info> asks = Lists.newArrayList();

    public Long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(Long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public List<Info> getBids() {
        return bids;
    }

    public List<Info> getAsks() {
        return asks;
    }

    public static class Info extends ToString {

        private BigDecimal price;
        private BigDecimal qty;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getQty() {
            return qty;
        }

        public void setQty(BigDecimal qty) {
            this.qty = qty;
        }
    }
}