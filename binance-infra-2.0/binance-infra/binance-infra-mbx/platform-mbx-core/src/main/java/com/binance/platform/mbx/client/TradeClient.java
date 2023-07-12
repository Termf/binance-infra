package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.trade.GetAggTradesRequest;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Li Fenggang
 * Date: 2020/8/4 5:10 下午
 */
public interface TradeClient {

    /**
     * It will be abandoned in the nearly future.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/public/aggTrades")
    @Deprecated
    public MbxResponse<String> getAggTrades(GetAggTradesRequest request) throws Exception;

}
