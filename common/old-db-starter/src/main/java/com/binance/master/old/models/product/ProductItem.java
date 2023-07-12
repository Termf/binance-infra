package com.binance.master.old.models.product;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ProductItem {
    private String symbol;
    private String name;
    private String initial;
    private String productType;
    private String typeName;
    private String status;
    private String pic;
    private String symbolType;
    private String baseAsset;
    private String quoteAsset;
    private String baseAssetUnit;
    private String quoteAssetUnit;
    private BigDecimal minQty;
    private BigDecimal maxQty;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private BigDecimal stepSize;
    private BigDecimal minNotional;
    private String description;
    private Integer decimalPlaces;
    private BigDecimal closePrice;
    private BigDecimal prevClose;
    private BigDecimal minTrade;
    private BigDecimal tickSize;
    private BigDecimal upLimit;
    private BigDecimal downLimit;
    private BigDecimal buyerCommission;
    private BigDecimal takerCommission;
    private BigDecimal makerCommission;
    private BigDecimal sellerCommission;
    private String buyerCommissionAsset;
    private String sellerCommissionAsset;
    private Long ruleId;
    private Boolean active;
    private Boolean offMarket;
    private String matchingUnitType;
    private Integer departmentId;
    private String custom1;
    private String custom2;
    private String custom3;
    private String simpleDesc;
    private String baseAssetName;
    private String quoteAssetName;
    private Integer test;

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProductItem))
            return false;
        return this.equals((ProductItem) other);
    }

    private boolean equals(ProductItem other) {
        return StringUtils.equals(this.symbol, other.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("symbol", symbol).add("name", name)
                .add("symbolType", symbolType).add("baseAsset", baseAsset).add("quoteAsset", quoteAsset).toString();
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getStepSize() {
        return stepSize;
    }

    public void setStepSize(BigDecimal stepSize) {
        this.stepSize = stepSize;
    }

    public BigDecimal getMinNotional() {
        return minNotional;
    }

    public void setMinNotional(BigDecimal minNotional) {
        this.minNotional = minNotional;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public BigDecimal getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(BigDecimal prevClose) {
        this.prevClose = prevClose;
    }

    public BigDecimal getMinTrade() {
        return minTrade;
    }

    public void setMinTrade(BigDecimal minTrade) {
        this.minTrade = minTrade;
    }

    public BigDecimal getTickSize() {
        return tickSize;
    }

    public void setTickSize(BigDecimal tickSize) {
        this.tickSize = tickSize;
    }

    public BigDecimal getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(BigDecimal upLimit) {
        this.upLimit = upLimit;
    }

    public BigDecimal getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(BigDecimal downLimit) {
        this.downLimit = downLimit;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
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

    public String getBuyerCommissionAsset() {
        return buyerCommissionAsset;
    }

    public void setBuyerCommissionAsset(String buyerCommissionAsset) {
        this.buyerCommissionAsset = buyerCommissionAsset;
    }

    public String getSellerCommissionAsset() {
        return sellerCommissionAsset;
    }

    public void setSellerCommissionAsset(String sellerCommissionAsset) {
        this.sellerCommissionAsset = sellerCommissionAsset;
    }

    public String getMatchingUnitType() {
        return matchingUnitType;
    }

    public void setMatchingUnitType(String matchingUnitType) {
        this.matchingUnitType = matchingUnitType;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public BigDecimal getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(BigDecimal takerCommission) {
        this.takerCommission = takerCommission;
    }

    public BigDecimal getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(BigDecimal makerCommission) {
        this.makerCommission = makerCommission;
    }

    public String getBaseAssetUnit() {
        return baseAssetUnit;
    }

    public void setBaseAssetUnit(String baseAssetUnit) {
        this.baseAssetUnit = baseAssetUnit;
    }

    public String getQuoteAssetUnit() {
        return quoteAssetUnit;
    }

    public void setQuoteAssetUnit(String quoteAssetUnit) {
        this.quoteAssetUnit = quoteAssetUnit;
    }

    public String getBaseAssetName() {
        return baseAssetName;
    }

    public void setBaseAssetName(String baseAssetName) {
        this.baseAssetName = baseAssetName;
    }

    public String getQuoteAssetName() {
        return quoteAssetName;
    }

    public void setQuoteAssetName(String quoteAssetName) {
        this.quoteAssetName = quoteAssetName;
    }

}
