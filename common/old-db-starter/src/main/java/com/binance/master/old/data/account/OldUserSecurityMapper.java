package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserSecurity;

@OldDB
public interface OldUserSecurityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserSecurity record);

    int insertSelective(OldUserSecurity record);

    OldUserSecurity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserSecurity record);

    int updateByPrimaryKey(OldUserSecurity record);

    @Monitored
    List<OldUserSecurity> selectByUserIds(@Param("userIds") List<String> userIds);

    @Monitored
    OldUserSecurity selectByUserId(@Param("userId") String userid);
}
