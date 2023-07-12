package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutOrderTypesRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;
    private boolean enableIceberg;
    private boolean enableMarket;
    private boolean enableStopLoss;
    private boolean enableStopLossLimit;
    private boolean enableTakeProfit;
    private boolean enableTakeProfitLimit;
    private boolean enableOco;

    private Boolean enableQuoteOrderQtyMarket = Boolean.TRUE;

    public PutOrderTypesRequest(@NotEmpty String symbol, boolean enableIceberg, boolean enableMarket,
                                boolean enableStopLoss, boolean enableStopLossLimit, boolean enableTakeProfit,
                                boolean enableTakeProfitLimit, boolean enableOco) {
        this.symbol = symbol;
        this.enableIceberg = enableIceberg;
        this.enableMarket = enableMarket;
        this.enableStopLoss = enableStopLoss;
        this.enableStopLossLimit = enableStopLossLimit;
        this.enableTakeProfit = enableTakeProfit;
        this.enableTakeProfitLimit = enableTakeProfitLimit;
        this.enableOco = enableOco;
    }

    public boolean isEnableIceberg() {
        return enableIceberg;
    }

    public void setEnableIceberg(boolean enableIceberg) {
        this.enableIceberg = enableIceberg;
    }

    public boolean isEnableMarket() {
        return enableMarket;
    }

    public void setEnableMarket(boolean enableMarket) {
        this.enableMarket = enableMarket;
    }

    public boolean isEnableStopLoss() {
        return enableStopLoss;
    }

    public void setEnableStopLoss(boolean enableStopLoss) {
        this.enableStopLoss = enableStopLoss;
    }

    public boolean isEnableStopLossLimit() {
        return enableStopLossLimit;
    }

    public void setEnableStopLossLimit(boolean enableStopLossLimit) {
        this.enableStopLossLimit = enableStopLossLimit;
    }

    public boolean isEnableTakeProfit() {
        return enableTakeProfit;
    }

    public void setEnableTakeProfit(boolean enableTakeProfit) {
        this.enableTakeProfit = enableTakeProfit;
    }

    public boolean isEnableTakeProfitLimit() {
        return enableTakeProfitLimit;
    }

    public void setEnableTakeProfitLimit(boolean enableTakeProfitLimit) {
        this.enableTakeProfitLimit = enableTakeProfitLimit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isEnableOco() {
        return enableOco;
    }

    public void setEnableOco(boolean enableOco) {
        this.enableOco = enableOco;
    }

    public Boolean getEnableQuoteOrderQtyMarket() {
        return enableQuoteOrderQtyMarket;
    }

    public void setEnableQuoteOrderQtyMarket(Boolean enableQuoteOrderQtyMarket) {
        this.enableQuoteOrderQtyMarket = enableQuoteOrderQtyMarket;
    }
}