package com.binance.master.old.data.charge;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldChargeAddress;

@OldDB
public interface OldChargeAddressMapper {

    OldChargeAddress getNewAddressByCoin(@Param("coin") String coin);

    int takeAddress(OldChargeAddress chargeAddress);
}
