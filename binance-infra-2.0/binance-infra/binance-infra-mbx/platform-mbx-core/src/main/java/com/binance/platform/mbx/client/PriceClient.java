package com.binance.platform.mbx.client;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.price.Get24hrTickerRequestV3;
import com.binance.platform.mbx.model.price.Get24hrTickerResponseV3;
import com.binance.platform.mbx.model.price.GetMbxPriceRequest;
import com.binance.platform.mbx.model.price.GetPriceRequest;
import com.binance.platform.mbx.model.price.GetTickerPriceRequest;
import com.binance.platform.mbx.model.price.SymbolClosePriceResponse;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface PriceClient {

    /**
     * 获取某个交易对的最新价格
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping("/public/price")
    Double getPrice(GetMbxPriceRequest request) throws MbxException;

    /**
     * 如果参数symbol不为空则获取其最新价格，否则获取所有交易对的最新价格
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping("/public/ticker-price")
    List<SymbolClosePriceResponse> getTickerPrice(GetTickerPriceRequest request)
            throws MbxException;

    /**
     * It will be abandoned in the near feature.
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping("/public/ticker/24hr")
    @Deprecated
    MbxResponse<String> get24hrTicker(GetPriceRequest request) throws MbxException;

    /**
     * 24hr 价格变动情况
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping("/get24hrTickerV3")
    MbxResponse<Get24hrTickerResponseV3> get24hrTickerV3(Get24hrTickerRequestV3 request) throws MbxException;
}
