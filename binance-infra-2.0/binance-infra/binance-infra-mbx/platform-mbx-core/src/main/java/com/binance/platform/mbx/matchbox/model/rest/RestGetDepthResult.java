package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 7:36 下午
 */
public class RestGetDepthResult extends MbxBaseModel {

    private Long lastUpdateId;
    private final List<List<BigDecimal>> bids = Lists.newArrayList();
    private final List<List<BigDecimal>> asks = Lists.newArrayList();

    public Long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(Long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public List<List<BigDecimal>> getBids() {
        return bids;
    }

    public List<List<BigDecimal>> getAsks() {
        return asks;
    }

}
