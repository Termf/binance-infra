package com.binance.mbx.sample.controller;

import com.binance.platform.mbx.client.TradeRuleClient;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.traderule.QueryAssetGasRequest;
import com.binance.platform.mbx.model.traderule.QueryUserProductFeeRequest;
import com.binance.platform.mbx.model.traderule.RefreshTradingRuleRequest;
import com.binance.platform.mbx.model.traderule.SetFeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Fenggang
 * Date: 2020/8/11 12:02 下午
 */
@RestController
@RequestMapping("/tradeRule")
public class TradeRuleController {

    @Autowired
    private TradeRuleClient tradeRuleClient;

    @PostMapping("/setAssetGas")
    public MbxResponse<Void> setAssetGas(QueryAssetGasRequest request) throws Exception {
        return tradeRuleClient.setAssetGas(request);
    }

    @PostMapping("/setUserProductFee")
    public MbxResponse<Void> setUserProductFee(QueryUserProductFeeRequest request) throws Exception {
        return tradeRuleClient.setUserProductFee(request);
    }

    @PostMapping("/setFee")
    public MbxResponse<Void> setFee(SetFeeRequest request) throws Exception {
        return tradeRuleClient.setFee(request);
    }

    @Deprecated
    @PostMapping("/refreshTradingRule")
    public MbxResponse<Void> refreshTradingRule(RefreshTradingRuleRequest request) throws Exception {
        return tradeRuleClient.refreshTradingRule(request);
    }
}
