package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUser;

@OldDB
public interface OldUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(OldUser record);

    int insertSelective(OldUser record);

    OldUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(OldUser record);

    int updateByPrimaryKey(OldUser record);

    List<OldUser> selectByMinUserIdAndMaxUserId(@Param("minUserId") String minUserId,
            @Param("maxUserId") String maxUserId, @Param("limit") Long limit);

    OldUser getUserByAccount(Long accountId);

    OldUser selectByEmailOnly(String email);
    
    public List<String> getEmptyPnkTradingAccount();

	List<String> getPnkTradingAccount();
}
