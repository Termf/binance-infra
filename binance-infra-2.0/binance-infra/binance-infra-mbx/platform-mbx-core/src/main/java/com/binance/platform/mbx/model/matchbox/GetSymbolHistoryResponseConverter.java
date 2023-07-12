package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolHistoryResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:40 上午
 */
public class GetSymbolHistoryResponseConverter {
    public static GetSymbolHistoryResponse convert(MbxGetSymbolHistoryResponse mbxGetSymbolHistoryResponse) {
        GetSymbolHistoryResponse getSymbolHistoryResponse = new GetSymbolHistoryResponse();
        getSymbolHistoryResponse.setStatus(mbxGetSymbolHistoryResponse.getStatus());
        getSymbolHistoryResponse.setTime(mbxGetSymbolHistoryResponse.getTime());
        return getSymbolHistoryResponse;
    }
}
