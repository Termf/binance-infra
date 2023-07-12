package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserForbiddenCode;

@OldDB
public interface OldUserForbiddenCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserForbiddenCode record);

    int insertSelective(OldUserForbiddenCode record);

    OldUserForbiddenCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserForbiddenCode record);

    int updateByPrimaryKey(OldUserForbiddenCode record);

    List<OldUserForbiddenCode> selectByUserIds(@Param("userIds") List<String> userIds);
}
