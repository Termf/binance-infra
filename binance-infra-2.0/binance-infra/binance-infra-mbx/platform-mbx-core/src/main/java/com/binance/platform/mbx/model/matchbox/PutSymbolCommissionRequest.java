package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutSymbolCommissionRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;
    private long buyerCommission;
    private long makerCommission;
    private long sellerCommission;
    private long takerCommission;

    public PutSymbolCommissionRequest(@NotEmpty String symbol, long buyerCommission, long makerCommission,
                                      long sellerCommission, long takerCommission) {
        this.symbol = symbol;
        this.buyerCommission = buyerCommission;
        this.makerCommission = makerCommission;
        this.sellerCommission = sellerCommission;
        this.takerCommission = takerCommission;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(long takerCommission) {
        this.takerCommission = takerCommission;
    }
}