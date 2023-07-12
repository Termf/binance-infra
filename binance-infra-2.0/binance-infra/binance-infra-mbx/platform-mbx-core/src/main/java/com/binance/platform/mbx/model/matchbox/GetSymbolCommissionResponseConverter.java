package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 6:04 上午
 */
public class GetSymbolCommissionResponseConverter {
    public static GetSymbolCommissionResponse convert(MbxGetSymbolCommissionResponse data) {
        GetSymbolCommissionResponse getSymbolCommissionResponse = new GetSymbolCommissionResponse();
        getSymbolCommissionResponse.setSymbol(data.getSymbol());
        getSymbolCommissionResponse.setEffectiveFrom(data.getEffectiveFrom());
        getSymbolCommissionResponse.setMakerCommission(data.getMakerCommission());
        getSymbolCommissionResponse.setTakerCommission(data.getTakerCommission());
        getSymbolCommissionResponse.setBuyerCommission(data.getBuyerCommission());
        getSymbolCommissionResponse.setSellerCommission(data.getSellerCommission());
        return getSymbolCommissionResponse;
    }
}
