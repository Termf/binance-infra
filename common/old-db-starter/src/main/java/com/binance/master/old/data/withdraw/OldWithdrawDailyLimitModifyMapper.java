package com.binance.master.old.data.withdraw;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldWithdrawDailyLimitModify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@OldDB
public interface OldWithdrawDailyLimitModifyMapper {
    int deleteByPrimaryKey(String userId);

    int insert(OldWithdrawDailyLimitModify record);

    int insertSelective(OldWithdrawDailyLimitModify record);

    OldWithdrawDailyLimitModify selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(OldWithdrawDailyLimitModify record);

    int updateByPrimaryKey(OldWithdrawDailyLimitModify record);

    List<OldWithdrawDailyLimitModify> getListByUserId(@Param("userIds") List<String> userIds);
}