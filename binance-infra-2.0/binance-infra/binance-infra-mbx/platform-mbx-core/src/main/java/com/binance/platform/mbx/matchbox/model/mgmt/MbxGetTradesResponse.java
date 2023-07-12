package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:21 上午
 */
public class MbxGetTradesResponse extends MbxBaseModel {

    /**
     * symbol : BTCUSDT
     * id : 3
     * buyerAcctId : 234
     * sellerAcctId : 149
     * buyerOrderId : 13
     * sellerOrderId : 5
     * buyerOrderListId : -1
     * sellerOrderListId : -1
     * buyCommission : 0.00000000
     * buyCommissionAsset : BTC
     * sellCommission : 0.09300000
     * sellCommissionAsset : USDT
     * price : 9300.00000000
     * qty : 0.01000000
     * time : 1594817236235
     * isBuyerMaker : false
     * isBestMatch : true
     * quoteQty : 93.00000000
     */

    private String symbol;
    private Long id;
    private Long buyerAcctId;
    private Long sellerAcctId;
    private Long buyerOrderId;
    private Long sellerOrderId;
    private Long buyerOrderListId;
    private Long sellerOrderListId;
    private BigDecimal buyCommission;
    private String buyCommissionAsset;
    private BigDecimal sellCommission;
    private String sellCommissionAsset;
    private BigDecimal price;
    private BigDecimal qty;
    private Long time;
    private Boolean isBuyerMaker;
    private Boolean isBestMatch;
    private BigDecimal quoteQty;
    private String msgAuth;

    public String getMsgAuth() {
        return msgAuth;
    }

    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerAcctId() {
        return buyerAcctId;
    }

    public void setBuyerAcctId(Long buyerAcctId) {
        this.buyerAcctId = buyerAcctId;
    }

    public Long getSellerAcctId() {
        return sellerAcctId;
    }

    public void setSellerAcctId(Long sellerAcctId) {
        this.sellerAcctId = sellerAcctId;
    }

    public Long getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(Long buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public Long getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(Long sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public Long getBuyerOrderListId() {
        return buyerOrderListId;
    }

    public void setBuyerOrderListId(Long buyerOrderListId) {
        this.buyerOrderListId = buyerOrderListId;
    }

    public Long getSellerOrderListId() {
        return sellerOrderListId;
    }

    public void setSellerOrderListId(Long sellerOrderListId) {
        this.sellerOrderListId = sellerOrderListId;
    }

    public BigDecimal getBuyCommission() {
        return buyCommission;
    }

    public void setBuyCommission(BigDecimal buyCommission) {
        this.buyCommission = buyCommission;
    }

    public String getBuyCommissionAsset() {
        return buyCommissionAsset;
    }

    public void setBuyCommissionAsset(String buyCommissionAsset) {
        this.buyCommissionAsset = buyCommissionAsset;
    }

    public BigDecimal getSellCommission() {
        return sellCommission;
    }

    public void setSellCommission(BigDecimal sellCommission) {
        this.sellCommission = sellCommission;
    }

    public String getSellCommissionAsset() {
        return sellCommissionAsset;
    }

    public void setSellCommissionAsset(String sellCommissionAsset) {
        this.sellCommissionAsset = sellCommissionAsset;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean isIsBuyerMaker() {
        return isBuyerMaker;
    }

    public void setIsBuyerMaker(Boolean isBuyerMaker) {
        this.isBuyerMaker = isBuyerMaker;
    }

    public Boolean isIsBestMatch() {
        return isBestMatch;
    }

    public void setIsBestMatch(Boolean isBestMatch) {
        this.isBestMatch = isBestMatch;
    }

    public BigDecimal getQuoteQty() {
        return quoteQty;
    }

    public void setQuoteQty(BigDecimal quoteQty) {
        this.quoteQty = quoteQty;
    }
}
