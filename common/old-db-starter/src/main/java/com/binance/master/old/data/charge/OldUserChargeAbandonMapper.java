package com.binance.master.old.data.charge;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldUserChargeAbandon;

@OldDB
public interface OldUserChargeAbandonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldUserChargeAbandon record);

    int insertSelective(OldUserChargeAbandon record);

    OldUserChargeAbandon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldUserChargeAbandon record);

    int updateByPrimaryKey(OldUserChargeAbandon record);
}
