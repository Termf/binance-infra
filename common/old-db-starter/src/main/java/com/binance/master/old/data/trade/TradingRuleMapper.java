package com.binance.master.old.data.trade;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.trade.RuleItem;
import org.apache.ibatis.annotations.Param;

@OldDB
public interface TradingRuleMapper {

    RuleItem getRuleByRuleId(@Param("ruleId") long ruleId);
}
