package com.binance.master.old.ibusiness.sys;

import com.binance.master.old.models.sys.TransactionRecord;

/**
 * 
 * @author Lip
 *
 */
public interface ITransactionRecordService {
    public TransactionRecord getTransActionById(Long id);

    public Long insertTransaction(TransactionRecord model);
    
    public TransactionRecord getTransactionByDateAndDescription(TransactionRecord item);
}
