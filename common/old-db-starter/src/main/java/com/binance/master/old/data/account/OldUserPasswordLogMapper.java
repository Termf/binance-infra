package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserPasswordLog;

@OldDB
public interface OldUserPasswordLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserPasswordLog record);

    int insertSelective(OldUserPasswordLog record);

    OldUserPasswordLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserPasswordLog record);

    int updateByPrimaryKey(OldUserPasswordLog record);

    List<OldUserPasswordLog> selectByUserIds(@Param("userIds") List<String> userIds);
}
