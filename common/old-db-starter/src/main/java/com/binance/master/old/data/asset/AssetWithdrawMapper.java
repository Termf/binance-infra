package com.binance.master.old.data.asset;

import com.binance.master.old.config.OldDB;

import java.util.List;
import java.util.Map;

@OldDB
public interface AssetWithdrawMapper {
    Long getWithdrawToday(Map<String, Object> param);

    List<Map<String, Object>> getVirtualWithdrawAmount(Map<String, Object> param);

    List<Map<String, Object>> assetWithdrawAmount(Map<String, Object> param);
}
