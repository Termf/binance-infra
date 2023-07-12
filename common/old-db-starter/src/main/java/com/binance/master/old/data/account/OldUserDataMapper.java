package com.binance.master.old.data.account;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserData;

@OldDB
public interface OldUserDataMapper {
    int insert(OldUserData record);

    int insertSelective(OldUserData record);

    @Monitored
    List<OldUserData> selectByUserIds(@Param("userIds") List<String> userIds);

    @Monitored
    OldUserData selectByUserId(@Param("userId") String userId);

    BigDecimal getTotalWithdrawAssetNum(String userId);
}
