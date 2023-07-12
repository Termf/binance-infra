package com.binance.master.old.data.charge;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldUserChargeAddress;
import com.binance.master.old.models.charge.OldUserChargeAddressKey;

@OldDB
public interface OldUserChargeAddressMapper {
    int deleteByPrimaryKey(OldUserChargeAddressKey key);

    int insert(OldUserChargeAddress record);

    int insertSelective(OldUserChargeAddress record);

    OldUserChargeAddress selectByPrimaryKey(OldUserChargeAddressKey key);

    int updateByPrimaryKeySelective(OldUserChargeAddress record);

    int updateByPrimaryKey(OldUserChargeAddress record);
    
    OldUserChargeAddress getLastAddress(@Param("userId") String userId, @Param("coin") String coin);

    public long getAddressSum(@Param("userId") String userId, @Param("coin") String coin);
}
