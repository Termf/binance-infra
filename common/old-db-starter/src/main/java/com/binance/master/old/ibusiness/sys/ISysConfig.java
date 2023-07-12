package com.binance.master.old.ibusiness.sys;

import com.binance.master.old.models.sys.SysConfig;

public interface ISysConfig {

    public SysConfig selectByDisplayName(String displayName);

}
