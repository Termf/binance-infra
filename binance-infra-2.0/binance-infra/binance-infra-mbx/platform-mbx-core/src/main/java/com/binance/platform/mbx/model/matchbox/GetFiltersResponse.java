package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:01 上午
 */
public class GetFiltersResponse extends ToString {

    private List<Api> api;
    private List<Engine> engine;

    public List<Api> getApi() {
        return api;
    }

    public void setApi(List<Api> api) {
        this.api = api;
    }

    public List<Engine> getEngine() {
        return engine;
    }

    public void setEngine(List<Engine> engine) {
        this.engine = engine;
    }

    public static class Api implements Serializable {
        /**
         * filterType : PRICE_FILTER
         * minPrice : 0.10000000
         * maxPrice : 10000.00000000
         * tickSize : 0.01000000
         * multiplierUp : 20
         * multiplierDown : 2
         * avgPriceMins : 1
         * minQty : 1.00000000
         * maxQty : 10.00000000
         * stepSize : 2.00000000
         * minNotional : 0.01000000
         * applyToMarket : true
         * limit : 10000
         */

        private String filterType;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private BigDecimal tickSize;
        private String multiplierUp;
        private String multiplierDown;
        private String avgPriceMins;
        private BigDecimal minQty;
        private BigDecimal maxQty;
        private BigDecimal stepSize;
        private BigDecimal minNotional;
        private Boolean applyToMarket;
        private Integer limit;

        public String getFilterType() {
            return filterType;
        }

        public void setFilterType(String filterType) {
            this.filterType = filterType;
        }

        public BigDecimal getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
        }

        public BigDecimal getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
        }

        public BigDecimal getTickSize() {
            return tickSize;
        }

        public void setTickSize(BigDecimal tickSize) {
            this.tickSize = tickSize;
        }

        public String getMultiplierUp() {
            return multiplierUp;
        }

        public void setMultiplierUp(String multiplierUp) {
            this.multiplierUp = multiplierUp;
        }

        public String getMultiplierDown() {
            return multiplierDown;
        }

        public void setMultiplierDown(String multiplierDown) {
            this.multiplierDown = multiplierDown;
        }

        public String getAvgPriceMins() {
            return avgPriceMins;
        }

        public void setAvgPriceMins(String avgPriceMins) {
            this.avgPriceMins = avgPriceMins;
        }

        public BigDecimal getMinQty() {
            return minQty;
        }

        public void setMinQty(BigDecimal minQty) {
            this.minQty = minQty;
        }

        public BigDecimal getMaxQty() {
            return maxQty;
        }

        public void setMaxQty(BigDecimal maxQty) {
            this.maxQty = maxQty;
        }

        public BigDecimal getStepSize() {
            return stepSize;
        }

        public void setStepSize(BigDecimal stepSize) {
            this.stepSize = stepSize;
        }

        public BigDecimal getMinNotional() {
            return minNotional;
        }

        public void setMinNotional(BigDecimal minNotional) {
            this.minNotional = minNotional;
        }

        public Boolean isApplyToMarket() {
            return applyToMarket;
        }

        public void setApplyToMarket(Boolean applyToMarket) {
            this.applyToMarket = applyToMarket;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }
    }

    public static class Engine implements Serializable {
        /**
         * filterType : MAX_NUM_ORDERS
         * maxNumOrders : 10
         * exemptAccounts : [0,1,2,3]
         * maxNumAlgoOrders : 10
         * maxNumIcebergOrders : 1
         * maxPosition : 8.00000000
         * endTime : 1528426903419
         */

        private String filterType;
        private Long maxNumOrders;
        private Long maxNumAlgoOrders;
        private Long maxNumIcebergOrders;
        private String maxPosition;
        private Long endTime;
        private List<Integer> exemptAccounts;

        public String getFilterType() {
            return filterType;
        }

        public void setFilterType(String filterType) {
            this.filterType = filterType;
        }

        public Long getMaxNumOrders() {
            return maxNumOrders;
        }

        public void setMaxNumOrders(Long maxNumOrders) {
            this.maxNumOrders = maxNumOrders;
        }

        public Long getMaxNumAlgoOrders() {
            return maxNumAlgoOrders;
        }

        public void setMaxNumAlgoOrders(Long maxNumAlgoOrders) {
            this.maxNumAlgoOrders = maxNumAlgoOrders;
        }

        public Long getMaxNumIcebergOrders() {
            return maxNumIcebergOrders;
        }

        public void setMaxNumIcebergOrders(Long maxNumIcebergOrders) {
            this.maxNumIcebergOrders = maxNumIcebergOrders;
        }

        public String getMaxPosition() {
            return maxPosition;
        }

        public void setMaxPosition(String maxPosition) {
            this.maxPosition = maxPosition;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public List<Integer> getExemptAccounts() {
            return exemptAccounts;
        }

        public void setExemptAccounts(List<Integer> exemptAccounts) {
            this.exemptAccounts = exemptAccounts;
        }
    }
}
