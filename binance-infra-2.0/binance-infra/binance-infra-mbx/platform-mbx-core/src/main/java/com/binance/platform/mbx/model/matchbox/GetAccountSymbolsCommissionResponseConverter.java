package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolsCommissionResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:08 上午
 */
public class GetAccountSymbolsCommissionResponseConverter {
    public static GetAccountSymbolsCommissionResponse convert(MbxGetAccountSymbolsCommissionResponse mbxGetAccountSymbolsCommissionResponse) {
        GetAccountSymbolsCommissionResponse getAccountSymbolsCommissionResponse = new GetAccountSymbolsCommissionResponse();
        getAccountSymbolsCommissionResponse.setSymbol(mbxGetAccountSymbolsCommissionResponse.getSymbol());
        getAccountSymbolsCommissionResponse.setEffectiveFrom(mbxGetAccountSymbolsCommissionResponse.getEffectiveFrom());
        getAccountSymbolsCommissionResponse.setMakerCommission(mbxGetAccountSymbolsCommissionResponse.getMakerCommission());
        getAccountSymbolsCommissionResponse.setTakerCommission(mbxGetAccountSymbolsCommissionResponse.getTakerCommission());
        getAccountSymbolsCommissionResponse.setBuyerCommission(mbxGetAccountSymbolsCommissionResponse.getBuyerCommission());
        getAccountSymbolsCommissionResponse.setSellerCommission(mbxGetAccountSymbolsCommissionResponse.getSellerCommission());
        return getAccountSymbolsCommissionResponse;
    }
}
