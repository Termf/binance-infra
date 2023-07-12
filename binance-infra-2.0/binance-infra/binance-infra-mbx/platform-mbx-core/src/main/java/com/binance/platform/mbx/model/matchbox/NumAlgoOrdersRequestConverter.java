package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.model.product.NumAlgoOrdersRequest;

/**
 * 设置下单频率的请求对象
 */
public class NumAlgoOrdersRequestConverter extends ToString {
    public static MbxPostNumAlgoOrdersFilterRequest convert(NumAlgoOrdersRequest source) {
        MbxPostNumAlgoOrdersFilterRequest mbxPostNumAlgoOrdersFilterRequest =
                new MbxPostNumAlgoOrdersFilterRequest(String.valueOf(source.getNumOrders()), source.getSymbol());
        return mbxPostNumAlgoOrdersFilterRequest;
    }
}
