package com.binance.mbx.sample.controller;

import com.binance.platform.mbx.client.KlineClient;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.kline.GetKlineRequest;
import com.binance.platform.mbx.model.kline.GetKlinesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Fenggang
 * Date: 2020/8/18 11:55 下午
 */
@RestController
@RequestMapping("/kline")
public class KlineController {
    @Autowired
    private KlineClient klineClient;

    @PostMapping("/klines")
    public MbxResponse<GetKlinesResponse> getKlines(GetKlineRequest request) throws Exception {
        return klineClient.getKlines(request);
    }
}
