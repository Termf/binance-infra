package com.binance.master.old.models.report;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalVerifyDto {

    public String asset;

    private BigDecimal exchangeInOutFlowTotal;

    private BigDecimal userAssetLogWithdraw;

    private BigDecimal userAssetLogCharge;

    private BigDecimal tradeExchangeIncome;

    private BigDecimal exchangeIncomeSum;

    private BigDecimal userAssetCurrentAmt;

}