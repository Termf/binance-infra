package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAccountV3Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:38 上午
 */
public class PostAccountResponseV3Converter {
    public static PostAccountResponseV3 convert(MbxPostAccountV3Response data) {
        PostAccountResponseV3 postAccountResponseV3 = new PostAccountResponseV3();
        postAccountResponseV3.setMakerCommission(data.getMakerCommission());
        postAccountResponseV3.setTakerCommission(data.getTakerCommission());
        postAccountResponseV3.setBuyerCommission(data.getBuyerCommission());
        postAccountResponseV3.setSellerCommission(data.getSellerCommission());
        postAccountResponseV3.setCanTrade(data.isCanTrade());
        postAccountResponseV3.setCanWithdraw(data.isCanWithdraw());
        postAccountResponseV3.setCanDeposit(data.isCanDeposit());
        postAccountResponseV3.setAccountId(data.getAccountId());
        postAccountResponseV3.setExternalId(data.getExternalId());
        postAccountResponseV3.setExternalAccountId(data.getExternalAccountId());
        postAccountResponseV3.setUpdateTime(data.getUpdateTime());
        postAccountResponseV3.setUpdateId(data.getUpdateId());
        postAccountResponseV3.setAccountType(data.getAccountType());
        postAccountResponseV3.setRestrictions(convert(data.getRestrictions()));
        // balance
        List<MbxPostAccountV3Response.Balance> mbxBalances = data.getBalances();
        if (mbxBalances != null) {
            List<PostAccountResponseV3.Balance> targetBalances = new ArrayList<>(mbxBalances.size());
            for (MbxPostAccountV3Response.Balance mbxBalance : mbxBalances) {
                targetBalances.add(convert(mbxBalance));
            }
            postAccountResponseV3.setBalances(targetBalances);
        } else {
            postAccountResponseV3.setBalances(Collections.emptyList());
        }
        postAccountResponseV3.setPermissions(data.getPermissions());
        return postAccountResponseV3;
    }

    private static PostAccountResponseV3.Balance convert(MbxPostAccountV3Response.Balance mbxBalance) {
        PostAccountResponseV3.Balance balance = new PostAccountResponseV3.Balance();
        balance.setAsset(mbxBalance.getAsset());
        balance.setFree(mbxBalance.getFree());
        balance.setLocked(mbxBalance.getLocked());
        balance.setExtLocked(mbxBalance.getExtLocked());
        return balance;
    }

    private static PostAccountResponseV3.Restrictions convert(MbxPostAccountV3Response.Restrictions mbxRestrictions) {
        PostAccountResponseV3.Restrictions restrictions = new PostAccountResponseV3.Restrictions();
        restrictions.setRestrictionId(mbxRestrictions.getRestrictionId());
        restrictions.setMode(mbxRestrictions.getMode());
        restrictions.setSymbols(mbxRestrictions.getSymbols());
        return restrictions;
    }
}
