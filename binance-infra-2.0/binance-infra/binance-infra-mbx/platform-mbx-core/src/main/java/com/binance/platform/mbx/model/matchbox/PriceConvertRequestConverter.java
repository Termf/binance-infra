package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.model.product.PriceConvertRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 8:08 下午
 */
public class PriceConvertRequestConverter {
    public static com.binance.platform.mbx.cloud.model.PriceConvertRequest convertFrom(PriceConvertRequest source) {
        com.binance.platform.mbx.cloud.model.PriceConvertRequest priceConvertRequest =
                new com.binance.platform.mbx.cloud.model.PriceConvertRequest();
        priceConvertRequest.setFrom(source.getFrom());
        priceConvertRequest.setTo(source.getTo());
        priceConvertRequest.setAmount(source.getAmount());
        priceConvertRequest.setDate(source.getDate());
        return priceConvertRequest;
    }
}
