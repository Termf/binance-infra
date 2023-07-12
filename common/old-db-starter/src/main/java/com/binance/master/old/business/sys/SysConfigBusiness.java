package com.binance.master.old.business.sys;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.binance.master.constant.CacheKeys;
import com.binance.master.old.config.OldDB;
import com.binance.master.old.data.sys.SysConfigMapper;
import com.binance.master.old.ibusiness.sys.ISysConfig;
import com.binance.master.old.models.sys.SysConfig;

/**
 * 获取老系统的配置
 * 
 * @author wang-bijie
 *
 */
@Service
public class SysConfigBusiness implements ISysConfig {

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Cacheable(value = CacheKeys.OLD_SYS_CONFIG, key = "#displayName")
    @Transactional(value = OldDB.TRANSACTION, propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    @Override
    public SysConfig selectByDisplayName(String displayName) {
        return this.sysConfigMapper.selectByDisplayName(displayName);
    }


}
