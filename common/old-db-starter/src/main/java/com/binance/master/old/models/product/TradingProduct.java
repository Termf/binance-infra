package com.binance.master.old.models.product;

import java.math.BigDecimal;

public class TradingProduct {
    private String symbol;

    private String status;

    private String symbolType;

    private String baseAsset;

    private String quoteAsset;

    private BigDecimal minQty;

    private BigDecimal maxQty;

    private BigDecimal maxPrice;

    private Integer decimalPlaces;

    private BigDecimal closePrice;

    private BigDecimal prevClose;

    private Integer ruleId;

    private Boolean active;

    private Boolean offMarket;

    private Boolean test;

    private BigDecimal buyerCommission;

    private BigDecimal sellerCommission;

    private BigDecimal makerCommission;

    private BigDecimal takerCommission;

    private String buyerCommissionAsset;

    private String sellerCommissionAsset;

    private String matchingUnitType;

    private Integer seqNum;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType == null ? null : symbolType.trim();
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset == null ? null : baseAsset.trim();
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset == null ? null : quoteAsset.trim();
    }

    public BigDecimal getMinQty() {
        return minQty;
    }

    public void setMinQty(BigDecimal minQty) {
        this.minQty = minQty;
    }

    public BigDecimal getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(BigDecimal maxQty) {
        this.maxQty = maxQty;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(BigDecimal prevClose) {
        this.prevClose = prevClose;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getOffMarket() {
        return offMarket;
    }

    public void setOffMarket(Boolean offMarket) {
        this.offMarket = offMarket;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public BigDecimal getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(BigDecimal buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public BigDecimal getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(BigDecimal sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public BigDecimal getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(BigDecimal makerCommission) {
        this.makerCommission = makerCommission;
    }

    public BigDecimal getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(BigDecimal takerCommission) {
        this.takerCommission = takerCommission;
    }

    public String getBuyerCommissionAsset() {
        return buyerCommissionAsset;
    }

    public void setBuyerCommissionAsset(String buyerCommissionAsset) {
        this.buyerCommissionAsset = buyerCommissionAsset == null ? null : buyerCommissionAsset.trim();
    }

    public String getSellerCommissionAsset() {
        return sellerCommissionAsset;
    }

    public void setSellerCommissionAsset(String sellerCommissionAsset) {
        this.sellerCommissionAsset = sellerCommissionAsset == null ? null : sellerCommissionAsset.trim();
    }

    public String getMatchingUnitType() {
        return matchingUnitType;
    }

    public void setMatchingUnitType(String matchingUnitType) {
        this.matchingUnitType = matchingUnitType == null ? null : matchingUnitType.trim();
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
