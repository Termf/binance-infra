package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostIcebergPartsFilterRequest;
import com.binance.platform.mbx.model.product.IceicebergLimitRequest;

/**
 * 设置冰山单拆分数量
 */
public class IceicebergLimitRequestConverter extends ToString {
    public static MbxPostIcebergPartsFilterRequest convert(IceicebergLimitRequest source) {
        MbxPostIcebergPartsFilterRequest mbxPostIcebergPartsFilterRequest =
                new MbxPostIcebergPartsFilterRequest(source.getLimit(), source.getSymbol());
        return mbxPostIcebergPartsFilterRequest;
    }
}
