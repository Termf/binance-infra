package com.binance.master.old.data.userData;

import com.binance.master.old.config.OldDB;

import java.util.List;

@OldDB
public interface UserDataMapper {
    List<String> selectSpecialUserIdList();
}
