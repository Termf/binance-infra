package com.binance.platform.mbx.model.product;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * 添加产品请求对象
 */
public class AddProductRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = 3959233536030938711L;
    @NotEmpty
    private String symbol;
    @NotEmpty
    private String baseAsset;
    @NotEmpty
    private String quoteAsset;
    private String symbolType;
    private BigDecimal minQty;
    private BigDecimal maxQty;
    private BigDecimal maxPrice;
    private String commissionType;
    private String matchingUnitType;
    private String mathSystemType;
    private Boolean allowSpot;
    private Boolean allowMargin;
    private Integer baseCommissionDecimalPlaces;
    private Integer quoteCommissionDecimalPlaces;

    public AddProductRequest(@NotEmpty String symbol, @NotEmpty String baseAsset, @NotEmpty String quoteAsset) {
        this.symbol = symbol;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
    }

    public String getSymbol() {
        return symbol;
    }
    /**
     * 交易对
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBaseAsset() {
        return baseAsset;
    }
    /**
     * 基础货币
     */
    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }
    /**
     * 标价货币
     */
    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
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

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getMatchingUnitType() {
        return matchingUnitType;
    }

    public void setMatchingUnitType(String matchingUnitType) {
        this.matchingUnitType = matchingUnitType;
    }

    public String getMathSystemType() {
        return mathSystemType;
    }

    public void setMathSystemType(String mathSystemType) {
        this.mathSystemType = mathSystemType;
    }

    public Boolean getAllowSpot() {
        return allowSpot;
    }
    /**
     * allowSpot defaults to true.
     * allowMargin defaults to false.
     * <p>
     * All existing symbols are considered to have allowSpot=true and allowMargin=false.
     * <p>
     * isSpotTradingAllowed and isMarginTradingAllowed are added to the symbolUpdate message on the private streamer.
     */
    public void setAllowSpot(Boolean allowSpot) {
        this.allowSpot = allowSpot;
    }

    public Boolean getAllowMargin() {
        return allowMargin;
    }

    /**
     * allowSpot defaults to true.
     * allowMargin defaults to false.
     * <p>
     * All existing symbols are considered to have allowSpot=true and allowMargin=false.
     * <p>
     * isSpotTradingAllowed and isMarginTradingAllowed are added to the symbolUpdate message on the private streamer.
     */
    public void setAllowMargin(Boolean allowMargin) {
        this.allowMargin = allowMargin;
    }

    public Integer getBaseCommissionDecimalPlaces() {
        return baseCommissionDecimalPlaces;
    }

    /**
     * This specifies the number of decimals to calculate precision to.
     */
    public void setBaseCommissionDecimalPlaces(Integer baseCommissionDecimalPlaces) {
        this.baseCommissionDecimalPlaces = baseCommissionDecimalPlaces;
    }

    public Integer getQuoteCommissionDecimalPlaces() {
        return quoteCommissionDecimalPlaces;
    }

    /**
     * This specifies the number of decimals to calculate precision to.
     */
    public void setQuoteCommissionDecimalPlaces(Integer quoteCommissionDecimalPlaces) {
        this.quoteCommissionDecimalPlaces = quoteCommissionDecimalPlaces;
    }
}
