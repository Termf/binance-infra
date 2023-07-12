package com.binance.master.old.business.userasset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.binance.master.old.data.userAsset.UserAssetLogMapper;
import com.binance.master.old.ibusiness.userasset.IUserAssetLogService;
import com.binance.master.old.models.userAsset.UserAssetLogWithdrawKey;
import com.google.common.collect.Sets;

/**
 * 获取老系统的配置
 * 
 * @author wang-bijie
 *
 */
@Service
public class UserAssetLogServiceImpl implements IUserAssetLogService {

    @Resource
    private UserAssetLogMapper userAssetLogMapper;

    @Override
    public List<UserAssetLogWithdrawKey> findRelationWithdrawRecord(Set<UserAssetLogWithdrawKey> keySet, Date date) {
        // 查询所有记录
        Set<UserAssetLogWithdrawKey> set = Sets.newHashSet(userAssetLogMapper.findRelationWithdrawRecord(date));
        List<UserAssetLogWithdrawKey> result = new ArrayList<>();
        for (UserAssetLogWithdrawKey key : keySet) {
            if (set.remove(key)) {
                result.add(key);
            }
        }
        return result;
    }


}
