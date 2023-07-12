package com.binance.master.old.data.withdraw;

import com.binance.master.commons.SearchResult;
import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldAssetWithdrawApprove;

import java.util.List;
import java.util.Map;

@OldDB
public interface OldAssetWithdrawApproveMapper {
    int insert(OldAssetWithdrawApprove record);

    OldAssetWithdrawApprove selectById(Integer id);

    int updateById(OldAssetWithdrawApprove record);

    List<OldAssetWithdrawApprove> getInitAssetWithdrawApproveByTranId(Long tranId);

    SearchResult<Map<String, Object>> getAssetWithdrawApprove(Map<String, Object> param);

    int updateAssetWithdrawStatus(Map<String, Object> param);

    long getWithdrawApproveByProposer(Map<String, Object> param);

    List<OldAssetWithdrawApprove> getAssetWithdrawApproveByIds(Map<String, Object> param);

    List<OldAssetWithdrawApprove> getAssetWithdrawApproveByWithdrawIds(Map<String, Object> param);

}