package com.binance.platform.mbx.model.traderule;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * set user product fee: input 产品号,买入卖出手续费，主动被动手续费 Request
 */
public class QueryUserProductFeeRequest extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = 4689681963033590099L;

    private long accountId;
    @NotEmpty
    private String symbol;
    private long buyFee;
    private long sellFee;
    private long takeFee;
    private long makeFee;

    public QueryUserProductFeeRequest(long accountId, @NotEmpty String symbol, long buyFee, long sellFee, long takeFee, long makeFee) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.takeFee = takeFee;
        this.makeFee = makeFee;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(long buyFee) {
        this.buyFee = buyFee;
    }

    public long getSellFee() {
        return sellFee;
    }

    public void setSellFee(long sellFee) {
        this.sellFee = sellFee;
    }

    public long getTakeFee() {
        return takeFee;
    }

    public void setTakeFee(long takeFee) {
        this.takeFee = takeFee;
    }

    public long getMakeFee() {
        return makeFee;
    }

    public void setMakeFee(long makeFee) {
        this.makeFee = makeFee;
    }
}
