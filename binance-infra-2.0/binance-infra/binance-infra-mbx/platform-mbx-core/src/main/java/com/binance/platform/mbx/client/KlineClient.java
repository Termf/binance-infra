package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.kline.GetKlineRequest;
import com.binance.platform.mbx.model.kline.GetKlinesResponse;
import org.springframework.web.bind.annotation.PostMapping;

public interface KlineClient {

    /**
     * Retrieve klines
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/public/klines")
    public MbxResponse<GetKlinesResponse> getKlines(GetKlineRequest request) throws Exception;
}
