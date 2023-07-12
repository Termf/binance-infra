package com.binance.mbx.sample.controller;

import com.binance.platform.mbx.client.PriceClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.price.Get24hrTickerRequestV3;
import com.binance.platform.mbx.model.price.Get24hrTickerResponseV3;
import com.binance.platform.mbx.model.price.GetMbxPriceRequest;
import com.binance.platform.mbx.model.price.GetPriceRequest;
import com.binance.platform.mbx.model.price.GetTickerPriceRequest;
import com.binance.platform.mbx.model.price.SymbolClosePriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/7 4:04 下午
 */
@RestController
@RequestMapping("/price")
public class PriceController {
    @Autowired
    private PriceClient priceClient;

    @PostMapping("/public/price")
    public Double getPrice(GetMbxPriceRequest request) throws MbxException {
        return priceClient.getPrice(request);
    }

    @PostMapping("/public/ticker-price")
    public List<SymbolClosePriceResponse> getTickerPrice(GetTickerPriceRequest request) throws MbxException {
        return priceClient.getTickerPrice(request);
    }

    @Deprecated
    @PostMapping("/public/ticker/24hr")
    public MbxResponse<String> get24hrTicker(GetPriceRequest request) throws MbxException {
        return priceClient.get24hrTicker(request);
    }

    @PostMapping("/get24hrTickerV3")
    public MbxResponse<Get24hrTickerResponseV3> get24hrTickerV3(Get24hrTickerRequestV3 request) throws MbxException {
        return priceClient.get24hrTickerV3(request);
    }
}
