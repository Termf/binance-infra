package com.binance.platform.mbx.model.product;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 设置下单类型的请求对象
 */
public class OrderTypeRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = -7865097945214495940L;
    /**
     * 交易对
     */
    @NotEmpty
    private String symbol;
    @NotNull
    private Boolean enableMarket;
    @NotNull
    private Boolean enableStopLoss;
    @NotNull
    private Boolean enableStopLossLimit;
    @NotNull
    private Boolean enableIceberg;
    @NotNull
    private Boolean enableTakeProfit;
    @NotNull
    private Boolean enableTakeProfitLimit;
    @NotNull
    private Boolean enableOco;
    // @NotEmpty
    private Boolean enableQuoteOrderQtyMarket = Boolean.TRUE;

    public OrderTypeRequest(@NotEmpty String symbol, @NotNull Boolean enableMarket, @NotNull Boolean enableStopLoss,
                            @NotNull Boolean enableStopLossLimit, @NotNull Boolean enableIceberg,
                            @NotNull Boolean enableTakeProfit, @NotNull Boolean enableTakeProfitLimit,
                            @NotNull Boolean enableOco) {
        this.symbol = symbol;
        this.enableMarket = enableMarket;
        this.enableStopLoss = enableStopLoss;
        this.enableStopLossLimit = enableStopLossLimit;
        this.enableIceberg = enableIceberg;
        this.enableTakeProfit = enableTakeProfit;
        this.enableTakeProfitLimit = enableTakeProfitLimit;
        this.enableOco = enableOco;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getEnableMarket() {
        return enableMarket;
    }

    public void setEnableMarket(Boolean enableMarket) {
        this.enableMarket = enableMarket;
    }

    public Boolean getEnableStopLoss() {
        return enableStopLoss;
    }

    public void setEnableStopLoss(Boolean enableStopLoss) {
        this.enableStopLoss = enableStopLoss;
    }

    public Boolean getEnableStopLossLimit() {
        return enableStopLossLimit;
    }

    public void setEnableStopLossLimit(Boolean enableStopLossLimit) {
        this.enableStopLossLimit = enableStopLossLimit;
    }

    public Boolean getEnableIceberg() {
        return enableIceberg;
    }

    public void setEnableIceberg(Boolean enableIceberg) {
        this.enableIceberg = enableIceberg;
    }

    public Boolean getEnableTakeProfit() {
        return enableTakeProfit;
    }

    public void setEnableTakeProfit(Boolean enableTakeProfit) {
        this.enableTakeProfit = enableTakeProfit;
    }

    public Boolean getEnableTakeProfitLimit() {
        return enableTakeProfitLimit;
    }

    public void setEnableTakeProfitLimit(Boolean enableTakeProfitLimit) {
        this.enableTakeProfitLimit = enableTakeProfitLimit;
    }

    public Boolean getEnableOco() {
        return enableOco;
    }

    public void setEnableOco(Boolean enableOco) {
        this.enableOco = enableOco;
    }

    public Boolean getEnableQuoteOrderQtyMarket() {
        return enableQuoteOrderQtyMarket;
    }

    public void setEnableQuoteOrderQtyMarket(Boolean enableQuoteOrderQtyMarket) {
        this.enableQuoteOrderQtyMarket = enableQuoteOrderQtyMarket;
    }
}
