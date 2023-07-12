package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import java.util.List;

public class QueryOpenOrderResponse extends ToString {
    private static final long serialVersionUID = 6155418839468736511L;
    private List<OpenOrderVo> openOrderList;

    public QueryOpenOrderResponse(final List<OpenOrderVo> openOrderList) {
        this.openOrderList = openOrderList;
    }

    public List<OpenOrderVo> getOpenOrderList() {
        return this.openOrderList;
    }

    public void setOpenOrderList(final List<OpenOrderVo> openOrderList) {
        this.openOrderList = openOrderList;
    }

    public QueryOpenOrderResponse() {
    }
}