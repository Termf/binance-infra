package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostSymbolRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbol";
    }

    @NotEmpty
    private String symbol;
    @NotEmpty
    private String symbolType;
    @NotEmpty
    private String baseAsset;
    @NotEmpty
    private String quoteAsset;
    @NotEmpty
    private String minQty;
    @NotEmpty
    private String maxQty;
    @NotEmpty
    private String maxPrice;
    @NotEmpty
    private String commissionType;
    /**
     * This specifies the number of decimals to calculate precision to.
     */
    private int baseCommissionDecimalPlaces;

    /**
     * This specifies the number of decimals to calculate precision to.
     */
    private int quoteCommissionDecimalPlaces;
    private String matchingUnitType;
    private String mathSystemType;
    private Boolean allowSpot;

    /**
     * allowSpot defaults to true.
     * allowMargin defaults to false.
     * <p>
     * All existing symbols are considered to have allowSpot=true and allowMargin=false.
     * <p>
     * isSpotTradingAllowed and isMarginTradingAllowed are added to the symbolUpdate message on the private streamer.
     */
    private Boolean allowMargin;

    private Integer permissionsBitmask;

    public MbxPostSymbolRequest(@NotEmpty String symbol, @NotEmpty String symbolType, @NotEmpty String baseAsset,
                                @NotEmpty String quoteAsset, @NotEmpty String minQty, @NotEmpty String maxQty,
                                @NotEmpty String maxPrice, @NotEmpty String commissionType,
                                int baseCommissionDecimalPlaces, int quoteCommissionDecimalPlaces) {
        this.symbol = symbol;
        this.symbolType = symbolType;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
        this.minQty = minQty;
        this.maxQty = maxQty;
        this.maxPrice = maxPrice;
        this.commissionType = commissionType;
        this.baseCommissionDecimalPlaces = baseCommissionDecimalPlaces;
        this.quoteCommissionDecimalPlaces = quoteCommissionDecimalPlaces;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
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

    public int getBaseCommissionDecimalPlaces() {
        return baseCommissionDecimalPlaces;
    }

    /**
     * This specifies the number of decimals to calculate precision to.
     */
    public void setBaseCommissionDecimalPlaces(int baseCommissionDecimalPlaces) {
        this.baseCommissionDecimalPlaces = baseCommissionDecimalPlaces;
    }

    public int getQuoteCommissionDecimalPlaces() {
        return quoteCommissionDecimalPlaces;
    }

    /**
     * This specifies the number of decimals to calculate precision to.
     */
    public void setQuoteCommissionDecimalPlaces(int quoteCommissionDecimalPlaces) {
        this.quoteCommissionDecimalPlaces = quoteCommissionDecimalPlaces;
    }

    public Integer getPermissionsBitmask() {
        return permissionsBitmask;
    }

    public void setPermissionsBitmask(Integer permissionsBitmask) {
        this.permissionsBitmask = permissionsBitmask;
    }
}
