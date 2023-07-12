package com.binance.master.old.business.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binance.master.old.data.sys.TransactionRecordMapper;
import com.binance.master.old.ibusiness.sys.ITransactionRecordService;
import com.binance.master.old.models.sys.TransactionRecord;

@Service("transactionRecordService")
public class TransactionRecordBusiness implements ITransactionRecordService {
    @Autowired
    private TransactionRecordMapper transactionRecordDao;

    @Override
    public TransactionRecord getTransActionById(Long id) {
        return transactionRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public Long insertTransaction(TransactionRecord model) {
        transactionRecordDao.insert(model);
        return model.getId();
    }

    @Override
    public TransactionRecord getTransactionByDateAndDescription(TransactionRecord model) {
        TransactionRecord tr = transactionRecordDao.selectByDateAndDescription(model);
        return tr;
    }

}
