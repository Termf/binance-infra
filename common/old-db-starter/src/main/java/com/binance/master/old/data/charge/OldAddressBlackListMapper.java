package com.binance.master.old.data.charge;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldAddressBlackList;
@OldDB
public interface OldAddressBlackListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldAddressBlackList record);

    int insertSelective(OldAddressBlackList record);

    OldAddressBlackList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldAddressBlackList record);

    int updateByPrimaryKey(OldAddressBlackList record);
    
    OldAddressBlackList  selectByAddress(String address);
}