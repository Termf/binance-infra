package com.binance.platform.mbx.model.kline;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class GetKlineRequest {
    @NotNull
    private String symbol;

    /** 时间间隔 */
    @NotNull
    private String interval;

    @Max(1000)
    private Long limit;

    private Long startTime;

    private Long endTime;

    /**
     *
     * @param symbol
     * @param interval 限制间隔 (interval)
     * <pre>K线图间隔参数:
     *
     * m -> 分钟; h -> 小时; d -> 天; w -> 周; M -> 月
     *
     * 1m
     * 3m
     * 5m
     * 15m
     * 30m
     * 1h
     * 2h
     * 4h
     * 6h
     * 8h
     * 12h
     * 1d
     * 3d
     * 1w
     * 1M
     * </pre>
     */
    public GetKlineRequest(@NotNull String symbol, @NotNull String interval) {
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