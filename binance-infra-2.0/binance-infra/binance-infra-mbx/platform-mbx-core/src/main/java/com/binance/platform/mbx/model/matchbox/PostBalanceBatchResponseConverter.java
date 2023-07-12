package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceBatchResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/9 5:12 下午
 */
public class PostBalanceBatchResponseConverter {
    public static PostBalanceBatchResponse convert(MbxPostBalanceBatchResponse mbxItem) {
        PostBalanceBatchResponse postBalanceBatchResponse = new PostBalanceBatchResponse();
        postBalanceBatchResponse.setSuccess(mbxItem.isSuccess());
        postBalanceBatchResponse.setAccountId(mbxItem.getAccountId());
        postBalanceBatchResponse.setExternalAccountId(mbxItem.getExternalAccountId());
        postBalanceBatchResponse.setTime(mbxItem.getTime());
        postBalanceBatchResponse.setMbxUpdateId(mbxItem.getMbxUpdateId());
        postBalanceBatchResponse.setExternalUpdateId(mbxItem.getExternalUpdateId());
        postBalanceBatchResponse.setAsset(mbxItem.getAsset());
        postBalanceBatchResponse.setAdjustmentAmount(mbxItem.getAdjustmentAmount());
        postBalanceBatchResponse.setFree(mbxItem.getFree());
        postBalanceBatchResponse.setLocked(mbxItem.getLocked());
        postBalanceBatchResponse.setExtLocked(mbxItem.getExtLocked());
        return postBalanceBatchResponse;
    }
}
