package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 1:44 下午
 */
public class RestGetKlinesRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/klines";
    }

    @NotNull
    private String symbol;

    /** 时间间隔 */
    @NotNull
    private String interval;

    @Max(1000)
    private Long limit;

    private Long startTime;

    private Long endTime;

    public RestGetKlinesRequest(@NotNull String symbol, @NotNull String interval) {
        this.symbol = symbol;
        this.interval = interval;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
