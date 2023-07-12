package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.master.error.GeneralCode;
import com.binance.master.utils.StringUtils;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * Get the price of a specified symbol
 *
 * @author Li Fenggang
 * Date: 2020/7/17 3:26 下午
 */
public class RestGetTickerPriceOneV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/ticker/price";
    }

    @NotEmpty
    private String symbol;

    public RestGetTickerPriceOneV3Request(@NotEmpty String symbol) {
        if (StringUtils.isBlank(symbol)) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "symbol can not be null");
        }
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
