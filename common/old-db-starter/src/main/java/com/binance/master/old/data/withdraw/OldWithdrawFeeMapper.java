package com.binance.master.old.data.withdraw;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldWithdrawFee;

@OldDB
public interface OldWithdrawFeeMapper {
    int insert(OldWithdrawFee record);

    int insertSelective(OldWithdrawFee record);
}
