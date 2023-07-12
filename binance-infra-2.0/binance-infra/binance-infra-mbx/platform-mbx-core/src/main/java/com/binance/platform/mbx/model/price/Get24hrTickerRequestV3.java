package com.binance.platform.mbx.model.price;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Get24hrTickerRequestV3
 *
 * @author Li Fenggang
 * Date: 2020/8/7 3:57 下午
 */
public class Get24hrTickerRequestV3 extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = -4108532536126753286L;

    @NotEmpty
    private String symbol;

    public Get24hrTickerRequestV3(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
