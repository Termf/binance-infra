package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

public class PriceConvertResponse extends ToString {
    private static final long serialVersionUID = 200428353391086170L;
    /** 若查找币种价格失败，直接返回0 */
    private BigDecimal price;
    /** 若查找币种价格失败，直接返回0 */
    private BigDecimal amount;

    public PriceConvertResponse() {
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }
}