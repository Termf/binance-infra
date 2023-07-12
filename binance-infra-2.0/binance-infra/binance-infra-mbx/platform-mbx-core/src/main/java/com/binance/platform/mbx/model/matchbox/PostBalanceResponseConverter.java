package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 11:32 下午
 */
public class PostBalanceResponseConverter {
    public static PostBalanceResponse convert(MbxPostBalanceResponse mbxPostBalanceResponse) {
        PostBalanceResponse postBalanceResponse = new PostBalanceResponse();
        postBalanceResponse.setAccountId(mbxPostBalanceResponse.getAccountId());
        postBalanceResponse.setExternalAccountId(mbxPostBalanceResponse.getExternalAccountId());
        postBalanceResponse.setTime(mbxPostBalanceResponse.getTime());
        postBalanceResponse.setMbxUpdateId(mbxPostBalanceResponse.getMbxUpdateId());
        postBalanceResponse.setExternalUpdateId(mbxPostBalanceResponse.getExternalUpdateId());
        postBalanceResponse.setAsset(mbxPostBalanceResponse.getAsset());
        postBalanceResponse.setAdjustmentAmount(mbxPostBalanceResponse.getAdjustmentAmount());
        postBalanceResponse.setFree(mbxPostBalanceResponse.getFree());
        postBalanceResponse.setLocked(mbxPostBalanceResponse.getLocked());
        postBalanceResponse.setExtLocked(mbxPostBalanceResponse.getExtLocked());
        return postBalanceResponse;

    }
}
