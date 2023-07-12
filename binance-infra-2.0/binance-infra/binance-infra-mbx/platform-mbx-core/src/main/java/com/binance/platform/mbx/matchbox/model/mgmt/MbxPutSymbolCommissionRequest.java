package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutSymbolCommissionRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbolCommission";
    }

    @NotEmpty
    private String symbol;
    private long buyerCommission;
    private long makerCommission;
    private long sellerCommission;
    private long takerCommission;

    public MbxPutSymbolCommissionRequest(@NotEmpty String symbol, long buyerCommission, long makerCommission, long sellerCommission, long takerCommission) {
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
