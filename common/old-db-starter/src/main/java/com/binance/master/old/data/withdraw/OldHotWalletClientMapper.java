package com.binance.master.old.data.withdraw;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldHotWalletClient;
import org.apache.ibatis.annotations.Param;

@OldDB
public interface OldHotWalletClientMapper {

    public OldHotWalletClient getHotWalletClientByName(@Param("clientName")String clientName);

}