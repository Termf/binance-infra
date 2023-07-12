package com.binance.master.old.data.sys;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.sys.SysConfig;

@OldDB
public interface SysConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    SysConfig selectByDisplayName(String displayName);
}
