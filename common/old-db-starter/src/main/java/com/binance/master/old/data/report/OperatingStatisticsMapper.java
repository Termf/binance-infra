package com.binance.master.old.data.report;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.report.OperatingStatisticsModel;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@OldDB
public interface OperatingStatisticsMapper {

    Integer deleteOperatingStatisticsByTime(@Param("time") String time);

    Map<String, Object> selectOneOperatingNearly(Map<String, Object> param);

    Integer insert(OperatingStatisticsModel operatingStatisticsModel);
}
