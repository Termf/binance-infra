package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:32 上午
 */
public class MbxGetAccountSymbolCommissionHistoryResponse extends MbxBaseModel {
    /**
     * symbol : ABCDEF
     * effectiveFrom : 1565861214925
     * makerCommission : 1
     * takerCommission : 2
     * buyerCommission : 3
     * sellerCommission : 4
     */

    private String symbol;
    private Long effectiveFrom;
    private Long makerCommission;
    private Long takerCommission;
    private Long buyerCommission;
    private Long sellerCommission;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Long effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(Long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public Long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(Long takerCommission) {
        this.takerCommission = takerCommission;
    }

    public Long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public Long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(Long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }
}
