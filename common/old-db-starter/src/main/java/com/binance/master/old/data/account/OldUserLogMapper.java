package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserLog;

@OldDB
public interface OldUserLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserLog record);

    int insertSelective(OldUserLog record);

    OldUserLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserLog record);

    int updateByPrimaryKey(OldUserLog record);

    @Monitored
    List<OldUserLog> selectByUserIds(@Param("userIds") List<String> userIds, @Param("hours") Integer hours);

    @Monitored
    OldUserLog selectLast(String userId);
}
