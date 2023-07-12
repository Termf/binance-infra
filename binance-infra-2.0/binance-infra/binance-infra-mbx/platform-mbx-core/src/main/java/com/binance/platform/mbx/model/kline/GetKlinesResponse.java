package com.binance.platform.mbx.model.kline;

import com.binance.master.commons.ToString;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class GetKlinesResponse extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = -3406041105648455318L;

    private final List<KLineItem> items = Lists.newArrayList();

    public List<KLineItem> getItems() {
        return items;
    }

    public static class KLineItem extends ToString {

        /**
         * 
         */
        private static final long serialVersionUID = -2824710479676072903L;

        private Long openTime;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
        private Long closeTime;
        private BigDecimal quoteAssetVolume;
        private Long numberOfTrades;
        private BigDecimal takerBuyBaseAssetVolume;
        private BigDecimal takerBuyQuoteAssetVolume;
        private BigDecimal ignore;

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public Long getOpenTime() {
            return openTime;
        }

        public void setOpenTime(Long openTime) {
            this.openTime = openTime;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
        }

        public Long getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(Long closeTime) {
            this.closeTime = closeTime;
        }

        public BigDecimal getQuoteAssetVolume() {
            return quoteAssetVolume;
        }

        public void setQuoteAssetVolume(BigDecimal quoteAssetVolume) {
            this.quoteAssetVolume = quoteAssetVolume;
        }

        public Long getNumberOfTrades() {
            return numberOfTrades;
        }

        public void setNumberOfTrades(Long numberOfTrades) {
            this.numberOfTrades = numberOfTrades;
        }

        public BigDecimal getTakerBuyBaseAssetVolume() {
            return takerBuyBaseAssetVolume;
        }

        public void setTakerBuyBaseAssetVolume(BigDecimal takerBuyBaseAssetVolume) {
            this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
        }

        public BigDecimal getTakerBuyQuoteAssetVolume() {
            return takerBuyQuoteAssetVolume;
        }

        public void setTakerBuyQuoteAssetVolume(BigDecimal takerBuyQuoteAssetVolume) {
            this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
        }

        public BigDecimal getIgnore() {
            return ignore;
        }

        public void setIgnore(BigDecimal ignore) {
            this.ignore = ignore;
        }
    }
}