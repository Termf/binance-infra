package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionHistoryResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:56 上午
 */
public class GetSymbolCommissionHistoryResponseConverter {
    public static GetSymbolCommissionHistoryResponse convert(MbxGetSymbolCommissionHistoryResponse mbxGetSymbolCommissionHistoryResponse) {
        GetSymbolCommissionHistoryResponse getSymbolCommissionHistoryResponse = new GetSymbolCommissionHistoryResponse();
        getSymbolCommissionHistoryResponse.setSymbol(mbxGetSymbolCommissionHistoryResponse.getSymbol());
        getSymbolCommissionHistoryResponse.setEffectiveFrom(mbxGetSymbolCommissionHistoryResponse.getEffectiveFrom());
        getSymbolCommissionHistoryResponse.setMakerCommission(mbxGetSymbolCommissionHistoryResponse.getMakerCommission());
        getSymbolCommissionHistoryResponse.setTakerCommission(mbxGetSymbolCommissionHistoryResponse.getTakerCommission());
        getSymbolCommissionHistoryResponse.setBuyerCommission(mbxGetSymbolCommissionHistoryResponse.getBuyerCommission());
        getSymbolCommissionHistoryResponse.setSellerCommission(mbxGetSymbolCommissionHistoryResponse.getSellerCommission());
        return getSymbolCommissionHistoryResponse;
    }
}
