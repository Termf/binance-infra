package com.binance.master.old.data.userCharge;

import com.binance.master.old.config.OldDB;

import java.util.List;
import java.util.Map;

@OldDB
public interface UserChargeMapper {
    Long getChargeToday(Map<String, Object> map);

    List<Map<String, Object>> getVirtualChargeAmount(Map<String, Object> map);

    List<Map<String, Object>> assetChargeAmount(Map<String, Object> map);
}
