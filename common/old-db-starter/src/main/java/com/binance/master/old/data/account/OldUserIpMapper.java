package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserIpKey;

@OldDB
public interface OldUserIpMapper {
    int deleteByPrimaryKey(OldUserIpKey key);

    int insert(OldUserIpKey record);

    int insertSelective(OldUserIpKey record);

    @Monitored
    List<OldUserIpKey> selectByUserIds(@Param("userIds") List<String> userIds);
}
