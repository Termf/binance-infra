package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolsResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:57 上午
 */
public class GetSymbolsResponseConverter {
    public static GetSymbolsResponse convert(MbxGetSymbolsResponse mbxGetSymbolsResponse) {
        GetSymbolsResponse getSymbolsResponse = new GetSymbolsResponse();
        getSymbolsResponse.setSymbol(mbxGetSymbolsResponse.getSymbol());
        getSymbolsResponse.setStatus(mbxGetSymbolsResponse.getStatus());
        getSymbolsResponse.setSymbolType(mbxGetSymbolsResponse.getSymbolType());
        getSymbolsResponse.setMatchingUnitType(mbxGetSymbolsResponse.getMatchingUnitType());
        getSymbolsResponse.setBaseAsset(mbxGetSymbolsResponse.getBaseAsset());
        getSymbolsResponse.setBasePrecision(mbxGetSymbolsResponse.getBasePrecision());
        getSymbolsResponse.setQuoteAsset(mbxGetSymbolsResponse.getQuoteAsset());
        getSymbolsResponse.setQuotePrecision(mbxGetSymbolsResponse.getQuotePrecision());
        getSymbolsResponse.setMinQty(mbxGetSymbolsResponse.getMinQty());
        getSymbolsResponse.setMaxQty(mbxGetSymbolsResponse.getMaxQty());
        getSymbolsResponse.setMaxPrice(mbxGetSymbolsResponse.getMaxPrice());
        getSymbolsResponse.setCommissionType(mbxGetSymbolsResponse.getCommissionType());
        getSymbolsResponse.setAllowStopLoss(mbxGetSymbolsResponse.isAllowStopLoss());
        getSymbolsResponse.setAllowStopLossLimit(mbxGetSymbolsResponse.isAllowStopLossLimit());
        getSymbolsResponse.setAllowIceberg(mbxGetSymbolsResponse.isAllowIceberg());
        getSymbolsResponse.setAllowTakeProfit(mbxGetSymbolsResponse.isAllowTakeProfit());
        getSymbolsResponse.setAllowTakeProfitLimit(mbxGetSymbolsResponse.isAllowTakeProfitLimit());
        getSymbolsResponse.setAllowOco(mbxGetSymbolsResponse.isAllowOco());
        getSymbolsResponse.setAllowQuoteOrderQtyMarket(mbxGetSymbolsResponse.isAllowQuoteOrderQtyMarket());
        getSymbolsResponse.setMathSystemType(mbxGetSymbolsResponse.getMathSystemType());
        getSymbolsResponse.setIsSpotTradingAllowed(mbxGetSymbolsResponse.isIsSpotTradingAllowed());
        getSymbolsResponse.setIsMarginTradingAllowed(mbxGetSymbolsResponse.isIsMarginTradingAllowed());
        getSymbolsResponse.setBaseCommissionPrecision(mbxGetSymbolsResponse.getBaseCommissionPrecision());
        getSymbolsResponse.setQuoteCommissionPrecision(mbxGetSymbolsResponse.getQuoteCommissionPrecision());
        getSymbolsResponse.setOrderTypes(mbxGetSymbolsResponse.getOrderTypes());
        getSymbolsResponse.setPermissions(mbxGetSymbolsResponse.getPermissions());
        return getSymbolsResponse;
    }
}
