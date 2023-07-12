package com.binance.master.old.data.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.sys.OldSysAddress;
@OldDB
public interface OldSysAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldSysAddress record);

    int insertSelective(OldSysAddress record);

    OldSysAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldSysAddress record);

    int updateByPrimaryKey(OldSysAddress record);
    
    List<OldSysAddress> queryByCoinAndLikeName(@Param("coin")String coin,@Param("name")String name);
}
