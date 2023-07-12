package com.binance.master.old.data.trade;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.trade.OldTradingLedger;
import com.binance.master.old.models.trade.TradeItem;

import java.util.List;
import java.util.Map;

@OldDB
public interface OldTradingLedgerMapper {
    int deleteByPrimaryKey(Long tranId);

    int insert(OldTradingLedger record);

    int insertSelective(OldTradingLedger record);

    OldTradingLedger selectByPrimaryKey(Long tranId);

    int updateByPrimaryKeySelective(OldTradingLedger record);

    int updateByPrimaryKey(OldTradingLedger record);

    OldTradingLedger fetchLedgerByTranId(Long tranId);

    Long fetchTradeCount(Map<String,Object> param);

    List<TradeItem> fetchTrade(Map<String,Object> param);

    List<Map<String,Object>> getMarketTradeNum(Map<String,Object> param);
    
    OldTradingLedger fetchLedgerBySideSymbolTrade(Map<String, Object> param);
}
