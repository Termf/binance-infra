package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.cloud.model.PriceConvertResponse;
import com.binance.platform.mbx.model.product.PriceConvertVo;

/**
 * 交易账户信息
 */
public class PriceConvertVoConverter {

	public static PriceConvertVo convertTo(PriceConvertResponse source) {
	    PriceConvertVo priceConvertVo = new PriceConvertVo();
	    priceConvertVo.setPrice(source.getPrice());
	    priceConvertVo.setAmount(source.getAmount());
	    return priceConvertVo;

	}
}
