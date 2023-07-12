package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldCompanyAuthentication;

@OldDB
public interface OldCompanyAuthenticationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OldCompanyAuthentication record);

    int insertSelective(OldCompanyAuthentication record);

    OldCompanyAuthentication selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OldCompanyAuthentication record);

    int updateByPrimaryKey(OldCompanyAuthentication record);

    @Monitored
    List<OldCompanyAuthentication> selectByUserIds(@Param("userIds") List<String> userIds);

    @Monitored
    List<OldCompanyAuthentication> selectByUserId(@Param("userId") String userId);
}
