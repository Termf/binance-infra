package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldSecurityPolicy;

@OldDB
public interface OldSecurityPolicyMapper {

    @Monitored
    List<OldSecurityPolicy> selectAll();
    
    OldSecurityPolicy selectByPrimaryKey(@Param("securityLevel") Integer securityLevel);

}
