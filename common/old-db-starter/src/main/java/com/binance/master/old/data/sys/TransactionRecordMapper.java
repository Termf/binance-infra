package com.binance.master.old.data.sys;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.sys.TransactionRecord;
import com.binance.master.old.models.transactionnRecord.QueryType;

import java.util.List;

@OldDB
public interface TransactionRecordMapper {
    public int deleteByPrimaryKey(Long id);

    public int insert(TransactionRecord record);

    public int insertSelective(TransactionRecord record);

    public TransactionRecord selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(TransactionRecord record);

    public int updateByPrimaryKey(TransactionRecord record);

    public Long selectLastId();

    public TransactionRecord selectByDateAndDescription(TransactionRecord model);

    public Long getCurrentTranId();

    public List<Long> selectByType(QueryType queryType);

    public TransactionRecord selectMinKey();
}
