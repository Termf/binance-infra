package com.binance.master.old.data.asset;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AssetPriceMapper {

    Long deleteAssetPrice();

    Long insertAssetPriceBatch(@Param("list") List<Map<String, Object>> list);
}
