package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 2:32 上午
 */
public class GetAccountResponseConverter {
    public static GetAccountResponse convert(MbxGetAccountResult mbxGetAccountResult) {
        GetAccountResponse getAccountResponse = new GetAccountResponse();
        getAccountResponse.setMakerCommission(mbxGetAccountResult.getMakerCommission());
        getAccountResponse.setTakerCommission(mbxGetAccountResult.getTakerCommission());
        getAccountResponse.setBuyerCommission(mbxGetAccountResult.getBuyerCommission());
        getAccountResponse.setSellerCommission(mbxGetAccountResult.getSellerCommission());
        getAccountResponse.setCanTrade(mbxGetAccountResult.isCanTrade());
        getAccountResponse.setCanWithdraw(mbxGetAccountResult.isCanWithdraw());
        getAccountResponse.setCanDeposit(mbxGetAccountResult.isCanDeposit());
        getAccountResponse.setAccountId(mbxGetAccountResult.getAccountId());
        getAccountResponse.setExternalId(mbxGetAccountResult.getExternalId());
        getAccountResponse.setExternalAccountId(mbxGetAccountResult.getExternalAccountId());
        getAccountResponse.setUpdateTime(mbxGetAccountResult.getUpdateTime());
        getAccountResponse.setUpdateId(mbxGetAccountResult.getUpdateId());
        getAccountResponse.setAccountType(mbxGetAccountResult.getAccountType());
        MbxGetAccountResult.Restrictions restrictions = mbxGetAccountResult.getRestrictions();
        getAccountResponse.setRestrictions(convert(restrictions));
        // balance
        List<MbxGetAccountResult.Balance> mbxBalances = mbxGetAccountResult.getBalances();
        if (mbxBalances != null) {
            List<GetAccountResponse.Balance> balances = new ArrayList<>(mbxBalances.size());
            for (MbxGetAccountResult.Balance mbxBalance : mbxBalances) {
                balances.add(convert(mbxBalance));
            }
            getAccountResponse.setBalances(balances);
        } else {
            getAccountResponse.setBalances(Collections.emptyList());
        }
        getAccountResponse.setPermissions(mbxGetAccountResult.getPermissions());
        return getAccountResponse;
    }

    private static GetAccountResponse.Balance convert(MbxGetAccountResult.Balance mbxBalance) {
        GetAccountResponse.Balance balance = new GetAccountResponse.Balance();
        balance.setAsset(mbxBalance.getAsset());
        balance.setFree(mbxBalance.getFree());
        balance.setLocked(mbxBalance.getLocked());
        balance.setExtLocked(mbxBalance.getExtLocked());
        return balance;
    }

    private static GetAccountResponse.Restrictions convert(MbxGetAccountResult.Restrictions mbxRestrictions) {
        GetAccountResponse.Restrictions restrictions = new GetAccountResponse.Restrictions();
        restrictions.setRestrictionId(mbxRestrictions.getRestrictionId());
        restrictions.setMode(mbxRestrictions.getMode());
        restrictions.setSymbols(mbxRestrictions.getSymbols());
        return restrictions;
    }
}
