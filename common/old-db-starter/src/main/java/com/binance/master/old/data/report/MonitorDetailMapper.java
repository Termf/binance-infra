package com.binance.master.old.data.report;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.report.OperatingStatisticsModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@OldDB
public interface MonitorDetailMapper {
    Long deleteAll();

    Long insertBatch(@Param("list") List<Map<String, Object>> list);
}
