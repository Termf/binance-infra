package com.binance.platform.mbx.model.matchbox;


import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAssetRequest;
import com.binance.platform.mbx.model.product.CreateAssetRequest;

/**
 * 创建Asset的请求对象
 */
public class CreateAssetRequestConverter {
   public static MbxPostAssetRequest convert(CreateAssetRequest source) {
       MbxPostAssetRequest mbxPostAssetRequest = new MbxPostAssetRequest(source.getAsset(), source.getDecimalPlaces());
       return mbxPostAssetRequest;
   }
}
