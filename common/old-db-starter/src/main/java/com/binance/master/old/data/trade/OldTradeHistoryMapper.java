package com.binance.master.old.data.trade;

import java.util.List;
import java.util.Map;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.trade.OldTradeHistory;

@OldDB
public interface OldTradeHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OldTradeHistory record);

    int insertSelective(OldTradeHistory record);

    OldTradeHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OldTradeHistory record);

    int updateByPrimaryKey(OldTradeHistory record);

    OldTradeHistory selectTrade(Map<String, Object> param);

    int selectCount(Map<String, Object> map);

    List<Map<String, Object>> selectPage(Map<String, Object> param);

    List<OldTradeHistory> selectByUnite(OldTradeHistory oldTradeHistory);

    List<Map<String, Object>> selectTradeIdAndSymbol(Map<String, Object> param);
}
