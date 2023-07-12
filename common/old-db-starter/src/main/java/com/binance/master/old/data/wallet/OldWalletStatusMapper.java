package com.binance.master.old.data.wallet;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.wallet.OldWalletStatus;

@OldDB
public interface OldWalletStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OldWalletStatus record);

    int insertSelective(OldWalletStatus record);

    OldWalletStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OldWalletStatus record);

    int updateByPrimaryKey(OldWalletStatus record);
}