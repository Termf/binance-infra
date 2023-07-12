package com.binance.master.old.data.account;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserOperationSummary;

@OldDB
public interface OldUserOperationSummaryMapper {
    int insert(OldUserOperationSummary record);

    int insertSelective(OldUserOperationSummary record);
}
