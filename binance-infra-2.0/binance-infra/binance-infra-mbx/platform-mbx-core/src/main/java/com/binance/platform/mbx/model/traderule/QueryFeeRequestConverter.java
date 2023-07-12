package com.binance.platform.mbx.model.traderule;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolCommissionRequest;

public class QueryFeeRequestConverter extends ToString {

    public static MbxPutSymbolCommissionRequest convert(SetFeeRequest source) {
        MbxPutSymbolCommissionRequest mbxPutSymbolCommissionRequest = new MbxPutSymbolCommissionRequest(
                source.getSymbol(),
                source.getBuyFee(),
                source.getMakeFee(),
                source.getSellFee(),
                source.getTakeFee()
        );
        return mbxPutSymbolCommissionRequest;

    }
}
