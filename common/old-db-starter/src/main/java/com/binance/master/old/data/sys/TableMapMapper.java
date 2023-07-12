package com.binance.master.old.data.sys;

import java.util.List;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.sys.TableMap;

@OldDB
public interface TableMapMapper {
    int insert(TableMap record);

    int insertSelective(TableMap record);

    List<TableMap> selectAll();
}
