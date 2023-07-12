package com.binance.master.old.data.report;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.report.OldExchangeFee;

@OldDB
public interface OldExchangeFeeMapper {
    int insert(OldExchangeFee record);

    int insertSelective(OldExchangeFee record);
}