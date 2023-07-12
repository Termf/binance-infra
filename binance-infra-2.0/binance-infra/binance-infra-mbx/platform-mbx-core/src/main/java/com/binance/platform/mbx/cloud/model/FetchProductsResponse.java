package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

public class FetchProductsResponse extends ToString {
    private static final long serialVersionUID = -2058586517382607389L;
    @ApiModelProperty
    private String symbol;
    @ApiModelProperty
    private String symbolType;
    @ApiModelProperty
    private String status;
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
    private BigDecimal tickSize;
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
    private String baseAssetName;
    @ApiModelProperty
    private String quoteAssetName;
    @ApiModelProperty
    private BigDecimal minTrade;
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
    private BigDecimal activeBuy;
    @ApiModelProperty
    private BigDecimal activeSell;
    @ApiModelProperty
    private BigDecimal closeUSDollar;
    @ApiModelProperty
    private BigDecimal closeRMB;
    @ApiModelProperty
    private String market;
    @ApiModelProperty
    private String marketName;
    @ApiModelProperty
    private String parentMarket;
    @ApiModelProperty
    private String parentMarketName;
    @ApiModelProperty("流通量")
    private Long circulatingSupply;

    public FetchProductsResponse() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getSymbolType() {
        return this.symbolType;
    }

    public String getStatus() {
        return this.status;
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

    public BigDecimal getTickSize() {
        return this.tickSize;
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

    public String getBaseAssetName() {
        return this.baseAssetName;
    }

    public String getQuoteAssetName() {
        return this.quoteAssetName;
    }

    public BigDecimal getMinTrade() {
        return this.minTrade;
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

    public Long getCirculatingSupply() {
        return this.circulatingSupply;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public void setSymbolType(final String symbolType) {
        this.symbolType = symbolType;
    }

    public void setStatus(final String status) {
        this.status = status;
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

    public void setTickSize(final BigDecimal tickSize) {
        this.tickSize = tickSize;
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

    public void setBaseAssetName(final String baseAssetName) {
        this.baseAssetName = baseAssetName;
    }

    public void setQuoteAssetName(final String quoteAssetName) {
        this.quoteAssetName = quoteAssetName;
    }

    public void setMinTrade(final BigDecimal minTrade) {
        this.minTrade = minTrade;
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

    public void setCirculatingSupply(final Long circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }
}