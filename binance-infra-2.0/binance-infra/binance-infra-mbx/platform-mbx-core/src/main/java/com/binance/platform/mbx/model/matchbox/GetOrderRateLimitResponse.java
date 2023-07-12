package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 6:07 上午
 */
public class GetOrderRateLimitResponse extends ToString {

    /**
     * rateInterval : DAY
     * intervalNum : 1
     * rateLimit : 100000
     * overrides : [{"accountId":6,"limit":2}]
     */

    private String rateInterval;
    private Long intervalNum;
    private Long rateLimit;
    private List<Override> overrides;

    public String getRateInterval() {
        return rateInterval;
    }

    public void setRateInterval(String rateInterval) {
        this.rateInterval = rateInterval;
    }

    public Long getIntervalNum() {
        return intervalNum;
    }

    public void setIntervalNum(Long intervalNum) {
        this.intervalNum = intervalNum;
    }

    public Long getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Long rateLimit) {
        this.rateLimit = rateLimit;
    }

    public List<Override> getOverrides() {
        return overrides;
    }

    public void setOverrides(List<Override> overrides) {
        this.overrides = overrides;
    }

    public static class Override implements Serializable {
        /**
         * accountId : 6
         * limit : 2
         */

        private Long accountId;
        private Long limit;

        public Long getAccountId() {
            return accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
        }

        public Long getLimit() {
            return limit;
        }

        public void setLimit(Long limit) {
            this.limit = limit;
        }
    }
}
