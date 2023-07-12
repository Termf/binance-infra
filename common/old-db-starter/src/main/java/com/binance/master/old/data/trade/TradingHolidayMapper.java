package com.binance.master.old.data.trade;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.trade.HolidayItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@OldDB
public interface TradingHolidayMapper {

    List<HolidayItem> getHolidays(Map<String, Object> param);

    void addHoliday(HolidayItem product);

    void removeHoliday(@Param("id") int id);

    Long isHoliday(@Param("day") Date day);

    int getHolidaysCount(Map<String, Object> param);

    void removeExpiredHoliday();
}
