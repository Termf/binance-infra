package com.binance.platform.mbx.model.price;

import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceV3Response;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 4:31 下午
 */
public class SymbolClosePriceResponseConverter {
    public static SymbolClosePriceResponse convertFrom(RestGetTickerPriceV3Response source) {
        SymbolClosePriceResponse symbolClosePriceResponse = new SymbolClosePriceResponse();
        symbolClosePriceResponse.setSymbol(source.getSymbol());
        symbolClosePriceResponse.setPrice(source.getPrice());
        return symbolClosePriceResponse;
    }
}
