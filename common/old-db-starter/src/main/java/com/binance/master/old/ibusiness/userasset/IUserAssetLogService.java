package com.binance.master.old.ibusiness.userasset;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.binance.master.old.models.userAsset.UserAssetLogWithdrawKey;

/**
 * 
 * @author Lip
 *
 */
public interface IUserAssetLogService {

    public List<UserAssetLogWithdrawKey> findRelationWithdrawRecord(Set<UserAssetLogWithdrawKey> keySet, Date date);

}
