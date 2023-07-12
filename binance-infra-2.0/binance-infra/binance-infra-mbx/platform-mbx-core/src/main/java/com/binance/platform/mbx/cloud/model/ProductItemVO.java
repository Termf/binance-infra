package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

public class ProductItemVO extends ToString {
    private static final long serialVersionUID = -3207200933126353273L;
    @ApiModelProperty
    private String symbol;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private String initial;
    @ApiModelProperty
    private String productType;
    @ApiModelProperty
    private String typeName;
    @ApiModelProperty
    private String status;
    @ApiModelProperty
    private String pic;
    @ApiModelProperty
    private String symbolType;
    @ApiModelProperty
    private String baseAsset;
    @ApiModelProperty
    private String quoteAsset;
    @ApiModelProperty
    private String baseAssetUnit;
    @ApiModelProperty
    private String quoteAssetUnit;
    @ApiModelProperty
    private BigDecimal minQty;
    @ApiModelProperty
    private BigDecimal maxQty;
    @ApiModelProperty
    private BigDecimal maxPrice;
    @ApiModelProperty
    private BigDecimal minPrice;
    @ApiModelProperty
    private BigDecimal stepSize;
    @ApiModelProperty
    private BigDecimal minNotional;
    @ApiModelProperty
    private String description;
    @ApiModelProperty
    private Integer decimalPlaces;
    @ApiModelProperty
    private BigDecimal closePrice;
    @ApiModelProperty
    private BigDecimal prevClose;
    @ApiModelProperty
    private BigDecimal minTrade;
    @ApiModelProperty
    private BigDecimal tickSize;
    @ApiModelProperty
    private BigDecimal upLimit;
    @ApiModelProperty
    private BigDecimal downLimit;
    @ApiModelProperty
    private BigDecimal buyerCommission;
    @ApiModelProperty
    private BigDecimal takerCommission;
    @ApiModelProperty
    private BigDecimal makerCommission;
    @ApiModelProperty
    private BigDecimal sellerCommission;
    @ApiModelProperty
    private String buyerCommissionAsset;
    @ApiModelProperty
    private String sellerCommissionAsset;
    @ApiModelProperty
    private Long ruleId;
    @ApiModelProperty
    private Boolean active;
    @ApiModelProperty
    private Boolean offMarket;
    @ApiModelProperty
    private String matchingUnitType;
    @ApiModelProperty
    private Integer departmentId;
    @ApiModelProperty
    private String custom1;
    @ApiModelProperty
    private String custom2;
    @ApiModelProperty
    private String custom3;
    @ApiModelProperty
    private String simpleDesc;
    @ApiModelProperty
    private String baseAssetName;
    @ApiModelProperty
    private String quoteAssetName;
    @ApiModelProperty
    private Integer test;
    @ApiModelProperty
    private String market;
    @ApiModelProperty
    private String marketName;
    @ApiModelProperty
    private String parentMarket;
    @ApiModelProperty
    private String parentMarketName;
    @ApiModelProperty
    private BigDecimal withdrawFee;
    @ApiModelProperty
    private BigDecimal open;
    private BigDecimal high;
    @ApiModelProperty
    private BigDecimal low;
    @ApiModelProperty
    private BigDecimal close;
    @ApiModelProperty
    private BigDecimal volume;
    @ApiModelProperty
    private BigDecimal tradedMoney;
    @ApiModelProperty
    private Long lastAggTradeId;
    @ApiModelProperty
    private BigDecimal activeBuy;
    @ApiModelProperty
    private BigDecimal activeSell;
    @ApiModelProperty
    private BigDecimal closeUSDollar;
    @ApiModelProperty
    private BigDecimal closeRMB;

    public ProductItemVO() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getName() {
        return this.name;
    }

    public String getInitial() {
        return this.initial;
    }

    public String getProductType() {
        return this.productType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getStatus() {
        return this.status;
    }

    public String getPic() {
        return this.pic;
    }

    public String getSymbolType() {
        return this.symbolType;
    }

    public String getBaseAsset() {
        return this.baseAsset;
    }

    public String getQuoteAsset() {
        return this.quoteAsset;
    }

    public String getBaseAssetUnit() {
        return this.baseAssetUnit;
    }

    public String getQuoteAssetUnit() {
        return this.quoteAssetUnit;
    }

    public BigDecimal getMinQty() {
        return this.minQty;
    }

    public BigDecimal getMaxQty() {
        return this.maxQty;
    }

    public BigDecimal getMaxPrice() {
        return this.maxPrice;
    }

    public BigDecimal getMinPrice() {
        return this.minPrice;
    }

    public BigDecimal getStepSize() {
        return this.stepSize;
    }

    public BigDecimal getMinNotional() {
        return this.minNotional;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public BigDecimal getClosePrice() {
        return this.closePrice;
    }

    public BigDecimal getPrevClose() {
        return this.prevClose;
    }

    public BigDecimal getMinTrade() {
        return this.minTrade;
    }

    public BigDecimal getTickSize() {
        return this.tickSize;
    }

    public BigDecimal getUpLimit() {
        return this.upLimit;
    }

    public BigDecimal getDownLimit() {
        return this.downLimit;
    }

    public BigDecimal getBuyerCommission() {
        return this.buyerCommission;
    }

    public BigDecimal getTakerCommission() {
        return this.takerCommission;
    }

    public BigDecimal getMakerCommission() {
        return this.makerCommission;
    }

    public BigDecimal getSellerCommission() {
        return this.sellerCommission;
    }

    public String getBuyerCommissionAsset() {
        return this.buyerCommissionAsset;
    }

    public String getSellerCommissionAsset() {
        return this.sellerCommissionAsset;
    }

    public Long getRuleId() {
        return this.ruleId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Boolean getOffMarket() {
        return this.offMarket;
    }

    public String getMatchingUnitType() {
        return this.matchingUnitType;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public String getCustom1() {
        return this.custom1;
    }

    public String getCustom2() {
        return this.custom2;
    }

    public String getCustom3() {
        return this.custom3;
    }

    public String getSimpleDesc() {
        return this.simpleDesc;
    }

    public String getBaseAssetName() {
        return this.baseAssetName;
    }

    public String getQuoteAssetName() {
        return this.quoteAssetName;
    }

    public Integer getTest() {
        return this.test;
    }

    public String getMarket() {
        return this.market;
    }

    public String getMarketName() {
        return this.marketName;
    }

    public String getParentMarket() {
        return this.parentMarket;
    }

    public String getParentMarketName() {
        return this.parentMarketName;
    }

    public BigDecimal getWithdrawFee() {
        return this.withdrawFee;
    }

    public BigDecimal getOpen() {
        return this.open;
    }

    public BigDecimal getHigh() {
        return this.high;
    }

    public BigDecimal getLow() {
        return this.low;
    }

    public BigDecimal getClose() {
        return this.close;
    }

    public BigDecimal getVolume() {
        return this.volume;
    }

    public BigDecimal getTradedMoney() {
        return this.tradedMoney;
    }

    public Long getLastAggTradeId() {
        return this.lastAggTradeId;
    }

    public BigDecimal getActiveBuy() {
        return this.activeBuy;
    }

    public BigDecimal getActiveSell() {
        return this.activeSell;
    }

    public BigDecimal getCloseUSDollar() {
        return this.closeUSDollar;
    }

    public BigDecimal getCloseRMB() {
        return this.closeRMB;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setInitial(final String initial) {
        this.initial = initial;
    }

    public void setProductType(final String productType) {
        this.productType = productType;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setPic(final String pic) {
        this.pic = pic;
    }

    public void setSymbolType(final String symbolType) {
        this.symbolType = symbolType;
    }

    public void setBaseAsset(final String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public void setQuoteAsset(final String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public void setBaseAssetUnit(final String baseAssetUnit) {
        this.baseAssetUnit = baseAssetUnit;
    }

    public void setQuoteAssetUnit(final String quoteAssetUnit) {
        this.quoteAssetUnit = quoteAssetUnit;
    }

    public void setMinQty(final BigDecimal minQty) {
        this.minQty = minQty;
    }

    public void setMaxQty(final BigDecimal maxQty) {
        this.maxQty = maxQty;
    }

    public void setMaxPrice(final BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(final BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setStepSize(final BigDecimal stepSize) {
        this.stepSize = stepSize;
    }

    public void setMinNotional(final BigDecimal minNotional) {
        this.minNotional = minNotional;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setDecimalPlaces(final Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setClosePrice(final BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public void setPrevClose(final BigDecimal prevClose) {
        this.prevClose = prevClose;
    }

    public void setMinTrade(final BigDecimal minTrade) {
        this.minTrade = minTrade;
    }

    public void setTickSize(final BigDecimal tickSize) {
        this.tickSize = tickSize;
    }

    public void setUpLimit(final BigDecimal upLimit) {
        this.upLimit = upLimit;
    }

    public void setDownLimit(final BigDecimal downLimit) {
        this.downLimit = downLimit;
    }

    public void setBuyerCommission(final BigDecimal buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public void setTakerCommission(final BigDecimal takerCommission) {
        this.takerCommission = takerCommission;
    }

    public void setMakerCommission(final BigDecimal makerCommission) {
        this.makerCommission = makerCommission;
    }

    public void setSellerCommission(final BigDecimal sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public void setBuyerCommissionAsset(final String buyerCommissionAsset) {
        this.buyerCommissionAsset = buyerCommissionAsset;
    }

    public void setSellerCommissionAsset(final String sellerCommissionAsset) {
        this.sellerCommissionAsset = sellerCommissionAsset;
    }

    public void setRuleId(final Long ruleId) {
        this.ruleId = ruleId;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public void setOffMarket(final Boolean offMarket) {
        this.offMarket = offMarket;
    }

    public void setMatchingUnitType(final String matchingUnitType) {
        this.matchingUnitType = matchingUnitType;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public void setCustom1(final String custom1) {
        this.custom1 = custom1;
    }

    public void setCustom2(final String custom2) {
        this.custom2 = custom2;
    }

    public void setCustom3(final String custom3) {
        this.custom3 = custom3;
    }

    public void setSimpleDesc(final String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public void setBaseAssetName(final String baseAssetName) {
        this.baseAssetName = baseAssetName;
    }

    public void setQuoteAssetName(final String quoteAssetName) {
        this.quoteAssetName = quoteAssetName;
    }

    public void setTest(final Integer test) {
        this.test = test;
    }

    public void setMarket(final String market) {
        this.market = market;
    }

    public void setMarketName(final String marketName) {
        this.marketName = marketName;
    }

    public void setParentMarket(final String parentMarket) {
        this.parentMarket = parentMarket;
    }

    public void setParentMarketName(final String parentMarketName) {
        this.parentMarketName = parentMarketName;
    }

    public void setWithdrawFee(final BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public void setOpen(final BigDecimal open) {
        this.open = open;
    }

    public void setHigh(final BigDecimal high) {
        this.high = high;
    }

    public void setLow(final BigDecimal low) {
        this.low = low;
    }

    public void setClose(final BigDecimal close) {
        this.close = close;
    }

    public void setVolume(final BigDecimal volume) {
        this.volume = volume;
    }

    public void setTradedMoney(final BigDecimal tradedMoney) {
        this.tradedMoney = tradedMoney;
    }

    public void setLastAggTradeId(final Long lastAggTradeId) {
        this.lastAggTradeId = lastAggTradeId;
    }

    public void setActiveBuy(final BigDecimal activeBuy) {
        this.activeBuy = activeBuy;
    }

    public void setActiveSell(final BigDecimal activeSell) {
        this.activeSell = activeSell;
    }

    public void setCloseUSDollar(final BigDecimal closeUSDollar) {
        this.closeUSDollar = closeUSDollar;
    }

    public void setCloseRMB(final BigDecimal closeRMB) {
        this.closeRMB = closeRMB;
    }
}