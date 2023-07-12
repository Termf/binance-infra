package com.binance.master.old.data.withdraw;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldWithdrawExepmtReviewAmount;

@OldDB
public interface OldWithdrawExepmtReviewAmountMapper {
    int deleteByPrimaryKey(String userId);

    int insert(OldWithdrawExepmtReviewAmount record);

    int insertSelective(OldWithdrawExepmtReviewAmount record);

    OldWithdrawExepmtReviewAmount selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(OldWithdrawExepmtReviewAmount record);

    int updateByPrimaryKey(OldWithdrawExepmtReviewAmount record);
}
