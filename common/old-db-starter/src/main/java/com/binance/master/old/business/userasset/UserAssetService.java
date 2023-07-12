package com.binance.master.old.business.userasset;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.data.userAsset.UserAssetMapper;
import com.binance.master.old.ibusiness.userasset.IUserAssetService;

/**
 * 获取老系统的配置
 * 
 * @author wang-bijie
 *
 */
@Service
public class UserAssetService implements IUserAssetService {

    @Resource
    private UserAssetMapper userAssetMapper;

    @Override
    @Transactional(value = OldDB.TRANSACTION, propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Map<String, Object>> getCheckWithdrawing(Map<String, Object> param) {
        return this.userAssetMapper.getCheckWithdrawing(param);
    }

    @Override
    @Transactional(value = OldDB.TRANSACTION, propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Map<String, Object>> getVerifyWithdrawing(Map<String, Object> param) {
        return this.userAssetMapper.getVerifyWithdrawing(param);
    }



}
