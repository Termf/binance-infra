package com.binance.master.old.models.report;

import java.math.BigDecimal;
import java.util.Date;

public class OperatingStatisticsModel {

    /**
     * 营运报表
     */
    // 日期
    private Date time;
    // 当日注册人数
    private Long registerToday;
    // 当日登录人数
    private Long loginToday;
    // 当日充值人数
    private Long chargeToday;
    // 当日下单人数
    private Long orderToday;
    // 当日交易人数
    private Long tradeToday;
    // 当日有持仓人数
    private Long positionToday;
    // 当日持仓大于0.01BTC人数
    private Long positionReallyToday;
    // 当日留存资产
    private BigDecimal retainAssetToday;
    // 当日真实留存资产
    private BigDecimal retainAsserReallyToday;
    // 当日成交量
    private BigDecimal tradeAmountToday;
    // 当日真实成交量
    private BigDecimal tradeAmountReallyToday;
    // 当日下单笔数
    private Long orderCountToday;
    // 当日真实下单笔数
    private Long orderCountReallyToday;
    // 当日成交笔数
    private Long tradeCountToday;
    // 当日真实下单笔数
    private Long tradeCountReallyToday;
    // 累计注册人数
    private Long registerTotal;
    // 累计登录人数
    private Long loginTotal;
    // 累计充值人数
    private Long chargeTotal;
    // 累计下单人数
    private Long orderTatol;
    // 累计交易人数
    private Long tradeTotal;
    // 当日提现人数
    private Long withdrawToday;
    // 累计提现人数
    private Long withdrawTotal;



    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getRegisterToday() {
        return registerToday;
    }

    public void setRegisterToday(Long registerToday) {
        this.registerToday = registerToday;
    }

    public Long getLoginToday() {
        return loginToday;
    }

    public void setLoginToday(Long loginToday) {
        this.loginToday = loginToday;
    }

    public Long getChargeToday() {
        return chargeToday;
    }

    public void setChargeToday(Long chargeToday) {
        this.chargeToday = chargeToday;
    }

    public Long getOrderToday() {
        return orderToday;
    }

    public void setOrderToday(Long orderToday) {
        this.orderToday = orderToday;
    }

    public Long getTradeToday() {
        return tradeToday;
    }

    public void setTradeToday(Long tradeToday) {
        this.tradeToday = tradeToday;
    }

    public Long getPositionToday() {
        return positionToday;
    }

    public void setPositionToday(Long positionToday) {
        this.positionToday = positionToday;
    }

    public BigDecimal getRetainAssetToday() {
        return retainAssetToday;
    }

    public void setRetainAssetToday(BigDecimal retainAssetToday) {
        this.retainAssetToday = retainAssetToday;
    }

    public BigDecimal getRetainAsserReallyToday() {
        return retainAsserReallyToday;
    }

    public void setRetainAsserReallyToday(BigDecimal retainAsserReallyToday) {
        this.retainAsserReallyToday = retainAsserReallyToday;
    }

    public BigDecimal getTradeAmountToday() {
        return tradeAmountToday;
    }

    public void setTradeAmountToday(BigDecimal tradeAmountToday) {
        this.tradeAmountToday = tradeAmountToday;
    }

    public BigDecimal getTradeAmountReallyToday() {
        return tradeAmountReallyToday;
    }

    public void setTradeAmountReallyToday(BigDecimal tradeAmountReallyToday) {
        this.tradeAmountReallyToday = tradeAmountReallyToday;
    }

    public Long getOrderCountToday() {
        return orderCountToday;
    }

    public void setOrderCountToday(Long orderCountToday) {
        this.orderCountToday = orderCountToday;
    }

    public Long getOrderCountReallyToday() {
        return orderCountReallyToday;
    }

    public void setOrderCountReallyToday(Long orderCountReallyToday) {
        this.orderCountReallyToday = orderCountReallyToday;
    }

    public Long getTradeCountToday() {
        return tradeCountToday;
    }

    public void setTradeCountToday(Long tradeCountToday) {
        this.tradeCountToday = tradeCountToday;
    }

    public Long getTradeCountReallyToday() {
        return tradeCountReallyToday;
    }

    public void setTradeCountReallyToday(Long tradeCountReallyToday) {
        this.tradeCountReallyToday = tradeCountReallyToday;
    }

    public Long getRegisterTotal() {
        return registerTotal;
    }

    public void setRegisterTotal(Long registerTotal) {
        this.registerTotal = registerTotal;
    }

    public Long getLoginTotal() {
        return loginTotal;
    }

    public void setLoginTotal(Long loginTotal) {
        this.loginTotal = loginTotal;
    }

    public Long getChargeTotal() {
        return chargeTotal;
    }

    public void setChargeTotal(Long chargeTotal) {
        this.chargeTotal = chargeTotal;
    }

    public Long getOrderTatol() {
        return orderTatol;
    }

    public void setOrderTatol(Long orderTatol) {
        this.orderTatol = orderTatol;
    }

    public Long getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(Long tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public Long getWithdrawToday() {
        return withdrawToday;
    }

    public void setWithdrawToday(Long withdrawToday) {
        this.withdrawToday = withdrawToday;
    }

    public Long getWithdrawTotal() {
        return withdrawTotal;
    }

    public void setWithdrawTotal(Long withdrawTotal) {
        this.withdrawTotal = withdrawTotal;
    }

    public Long getPositionReallyToday() {
        return positionReallyToday;
    }

    public void setPositionReallyToday(Long positionReallyToday) {
        this.positionReallyToday = positionReallyToday;
    }
}
