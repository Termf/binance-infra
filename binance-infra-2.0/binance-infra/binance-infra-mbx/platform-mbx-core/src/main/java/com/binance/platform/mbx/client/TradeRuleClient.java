package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.traderule.QueryAssetGasRequest;
import com.binance.platform.mbx.model.traderule.SetFeeRequest;
import com.binance.platform.mbx.model.traderule.QueryUserProductFeeRequest;
import com.binance.platform.mbx.model.traderule.RefreshTradingRuleRequest;
import org.springframework.web.bind.annotation.PostMapping;

public interface TradeRuleClient {
    // 30天无日志的，认为已经废弃了-------------------------------------------------------------------------------
    // setTradingMode
    // fetchTimeRule
    // setGlobalConfig
    // useGlobalConfig
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------
    // products
    // -------------------------------------------------------------------------------
    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/setAssetGas")
    public  MbxResponse<Void> setAssetGas(QueryAssetGasRequest request) throws Exception;

    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/setUserProductFee")
    public MbxResponse<Void> setUserProductFee(QueryUserProductFeeRequest request)
            throws Exception;

    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/setFee")
    public  MbxResponse<Void> setFee(SetFeeRequest request) throws Exception;

    /**
     * 刷新交易策略
     * 
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/refreshTradingRule")
    @Deprecated
    public  MbxResponse<Void> refreshTradingRule(RefreshTradingRuleRequest request)
            throws Exception;
}
