package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@OldDB
public interface DailySnapshotProductPriceMapper {
    Long deletePriceByDate(@Param("time") String time);

    Long saveBatch(@Param("list") List<Map<String, Object>> list);

    Long updatePrevClose(Map<String, Object> param);

}
