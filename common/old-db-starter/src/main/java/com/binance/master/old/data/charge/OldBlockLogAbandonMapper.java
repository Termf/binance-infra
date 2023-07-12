package com.binance.master.old.data.charge;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldBlockLogAbandon;

@OldDB
public interface OldBlockLogAbandonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldBlockLogAbandon record);

    int insertSelective(OldBlockLogAbandon record);

    OldBlockLogAbandon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldBlockLogAbandon record);

    int updateByPrimaryKey(OldBlockLogAbandon record);
}