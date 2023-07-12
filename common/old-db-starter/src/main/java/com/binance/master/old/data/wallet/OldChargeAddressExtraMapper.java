package com.binance.master.old.data.wallet;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.wallet.OldChargeAddressExtra;
import com.binance.master.old.models.wallet.OldChargeAddressExtraKey;

@OldDB
public interface OldChargeAddressExtraMapper {
    int deleteByPrimaryKey(OldChargeAddressExtraKey key);

    int insert(OldChargeAddressExtra record);

    int insertSelective(OldChargeAddressExtra record);

    OldChargeAddressExtra selectByPrimaryKey(OldChargeAddressExtraKey key);

    int updateByPrimaryKeySelective(OldChargeAddressExtra record);

    int updateByPrimaryKey(OldChargeAddressExtra record);
    
    List<OldChargeAddressExtra> getChargeAddressExtra(@Param("date")String date);
}