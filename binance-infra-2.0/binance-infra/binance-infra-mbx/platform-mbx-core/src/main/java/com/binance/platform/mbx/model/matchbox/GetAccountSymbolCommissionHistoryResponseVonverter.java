package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolCommissionHistoryResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:36 上午
 */
public class GetAccountSymbolCommissionHistoryResponseVonverter {
    public static GetAccountSymbolCommissionHistoryResponse convert(MbxGetAccountSymbolCommissionHistoryResponse mbxGetAccountSymbolCommissionHistoryResponse) {
        GetAccountSymbolCommissionHistoryResponse getAccountSymbolCommissionHistoryResponse = new GetAccountSymbolCommissionHistoryResponse();
        getAccountSymbolCommissionHistoryResponse.setSymbol(mbxGetAccountSymbolCommissionHistoryResponse.getSymbol());
        getAccountSymbolCommissionHistoryResponse.setEffectiveFrom(mbxGetAccountSymbolCommissionHistoryResponse.getEffectiveFrom());
        getAccountSymbolCommissionHistoryResponse.setMakerCommission(mbxGetAccountSymbolCommissionHistoryResponse.getMakerCommission());
        getAccountSymbolCommissionHistoryResponse.setTakerCommission(mbxGetAccountSymbolCommissionHistoryResponse.getTakerCommission());
        getAccountSymbolCommissionHistoryResponse.setBuyerCommission(mbxGetAccountSymbolCommissionHistoryResponse.getBuyerCommission());
        getAccountSymbolCommissionHistoryResponse.setSellerCommission(mbxGetAccountSymbolCommissionHistoryResponse.getSellerCommission());
        return getAccountSymbolCommissionHistoryResponse;
    }
}