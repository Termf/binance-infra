package com.binance.platform.mbx.model.traderule;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountSymbolsCommissionRequest;

import java.util.Arrays;

/**
 * set user product fee: input 产品号,买入卖出手续费，主动被动手续费 Request
 */
public class QueryUserProductFeeRequestConverter {
    public static MbxPutAccountSymbolsCommissionRequest convert(QueryUserProductFeeRequest source) {
        MbxPutAccountSymbolsCommissionRequest mbxPutAccountSymbolsCommissionRequest =
                new MbxPutAccountSymbolsCommissionRequest(source.getAccountId(),
                        Arrays.asList(source.getSymbol()), source.getBuyFee(),
                        source.getMakeFee(), source.getSellFee(), source.getTakeFee()
                );
        return mbxPutAccountSymbolsCommissionRequest;
    }
}
