package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutOrderTypesRequest;
import com.binance.platform.mbx.model.product.OrderTypeRequest;

/**
 * 设置下单类型的请求对象
 */
public class OrderTypeRequestConverter extends ToString {
    public static MbxPutOrderTypesRequest convert(OrderTypeRequest source) {
        MbxPutOrderTypesRequest mbxPutOrderTypesRequest = new MbxPutOrderTypesRequest(
                source.getSymbol(),
                source.getEnableIceberg(),
                source.getEnableMarket(),
                source.getEnableStopLoss(),
                source.getEnableStopLossLimit(),
                source.getEnableTakeProfit(),
                source.getEnableTakeProfitLimit(),
                source.getEnableOco()
        );
        mbxPutOrderTypesRequest.setEnableQuoteOrderQtyMarket(source.getEnableQuoteOrderQtyMarket());
        return mbxPutOrderTypesRequest;

    }
}
