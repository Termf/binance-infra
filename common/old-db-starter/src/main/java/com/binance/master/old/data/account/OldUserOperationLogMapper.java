package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserOperationLog;

@OldDB
public interface OldUserOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserOperationLog record);

    int insertSelective(OldUserOperationLog record);

    OldUserOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserOperationLog record);

    int updateByPrimaryKey(OldUserOperationLog record);

    List<OldUserOperationLog> selectByUserIds(@Param("userIds") List<String> userIds);
}
