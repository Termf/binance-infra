package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:49 上午
 */
public class GetSymbolsResponse extends ToString {

    /**
     * symbol : BTCUSDT
     * status : TRADING
     * symbolType : SPOT
     * matchingUnitType : STANDARD
     * baseAsset : BTC
     * basePrecision : 8
     * quoteAsset : USDT
     * quotePrecision : 8
     * minQty : 0.10000000
     * maxQty : 30.00000000
     * maxPrice : 10000.00000000
     * commissionType : GAS_OR_RECV_OPTIONAL
     * orderTypes : ["LIMIT","LIMIT_MAKER","MARKET","STOP_LOSS","STOP_LOSS_LIMIT","TAKE_PROFIT","TAKE_PROFIT_LIMIT"]
     * allowStopLoss : true
     * allowStopLossLimit : true
     * allowIceberg : true
     * allowTakeProfit : true
     * allowTakeProfitLimit : true
     * allowOco : true
     * allowQuoteOrderQtyMarket : true
     * mathSystemType : FULL_CUT
     * isSpotTradingAllowed : false
     * isMarginTradingAllowed : true
     * permissions : ["UNKNOWN_1"]
     * baseCommissionPrecision : 8
     * quoteCommissionPrecision : 8
     */

    private String symbol;
    private String status;
    private String symbolType;
    private String matchingUnitType;
    private String baseAsset;
    private Integer basePrecision;
    private String quoteAsset;
    private Integer quotePrecision;
    private BigDecimal minQty;
    private BigDecimal maxQty;
    private BigDecimal maxPrice;
    private String commissionType;
    private Boolean allowStopLoss;
    private Boolean allowStopLossLimit;
    private Boolean allowIceberg;
    private Boolean allowTakeProfit;
    private Boolean allowTakeProfitLimit;
    private Boolean allowOco;
    private Boolean allowQuoteOrderQtyMarket;
    private String mathSystemType;
    private Boolean isSpotTradingAllowed;
    private Boolean isMarginTradingAllowed;
    private Integer baseCommissionPrecision;
    private Integer quoteCommissionPrecision;
    private List<String> orderTypes;
    private List<String> permissions;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public Integer getBasePrecision() {
        return basePrecision;
    }

    public void setBasePrecision(Integer basePrecision) {
        this.basePrecision = basePrecision;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public Integer getQuotePrecision() {
        return quotePrecision;
    }

    public void setQuotePrecision(Integer quotePrecision) {
        this.quotePrecision = quotePrecision;
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

    public Boolean isAllowStopLoss() {
        return allowStopLoss;
    }

    public void setAllowStopLoss(Boolean allowStopLoss) {
        this.allowStopLoss = allowStopLoss;
    }

    public Boolean isAllowStopLossLimit() {
        return allowStopLossLimit;
    }

    public void setAllowStopLossLimit(Boolean allowStopLossLimit) {
        this.allowStopLossLimit = allowStopLossLimit;
    }

    public Boolean isAllowIceberg() {
        return allowIceberg;
    }

    public void setAllowIceberg(Boolean allowIceberg) {
        this.allowIceberg = allowIceberg;
    }

    public Boolean isAllowTakeProfit() {
        return allowTakeProfit;
    }

    public void setAllowTakeProfit(Boolean allowTakeProfit) {
        this.allowTakeProfit = allowTakeProfit;
    }

    public Boolean isAllowTakeProfitLimit() {
        return allowTakeProfitLimit;
    }

    public void setAllowTakeProfitLimit(Boolean allowTakeProfitLimit) {
        this.allowTakeProfitLimit = allowTakeProfitLimit;
    }

    public Boolean isAllowOco() {
        return allowOco;
    }

    public void setAllowOco(Boolean allowOco) {
        this.allowOco = allowOco;
    }

    public Boolean isAllowQuoteOrderQtyMarket() {
        return allowQuoteOrderQtyMarket;
    }

    public void setAllowQuoteOrderQtyMarket(Boolean allowQuoteOrderQtyMarket) {
        this.allowQuoteOrderQtyMarket = allowQuoteOrderQtyMarket;
    }

    public String getMathSystemType() {
        return mathSystemType;
    }

    public void setMathSystemType(String mathSystemType) {
        this.mathSystemType = mathSystemType;
    }

    public Boolean isIsSpotTradingAllowed() {
        return isSpotTradingAllowed;
    }

    public void setIsSpotTradingAllowed(Boolean isSpotTradingAllowed) {
        this.isSpotTradingAllowed = isSpotTradingAllowed;
    }

    public Boolean isIsMarginTradingAllowed() {
        return isMarginTradingAllowed;
    }

    public void setIsMarginTradingAllowed(Boolean isMarginTradingAllowed) {
        this.isMarginTradingAllowed = isMarginTradingAllowed;
    }

    public Integer getBaseCommissionPrecision() {
        return baseCommissionPrecision;
    }

    public void setBaseCommissionPrecision(Integer baseCommissionPrecision) {
        this.baseCommissionPrecision = baseCommissionPrecision;
    }

    public Integer getQuoteCommissionPrecision() {
        return quoteCommissionPrecision;
    }

    public void setQuoteCommissionPrecision(Integer quoteCommissionPrecision) {
        this.quoteCommissionPrecision = quoteCommissionPrecision;
    }

    public List<String> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(List<String> orderTypes) {
        this.orderTypes = orderTypes;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
