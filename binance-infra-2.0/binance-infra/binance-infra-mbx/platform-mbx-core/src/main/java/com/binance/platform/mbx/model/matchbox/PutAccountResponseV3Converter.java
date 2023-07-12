package com.binance.platform.mbx.model.matchbox;
import com.google.common.collect.Lists;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountResponseV3;

import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/9 6:17 下午
 */
public class PutAccountResponseV3Converter {
    public static PutAccountResponseV3 convert(MbxPutAccountResponseV3 mbxPutAccountResponseV3) {
        PutAccountResponseV3 putAccountResponseV3 = new PutAccountResponseV3();
        putAccountResponseV3.setMakerCommission(mbxPutAccountResponseV3.getMakerCommission());
        putAccountResponseV3.setTakerCommission(mbxPutAccountResponseV3.getTakerCommission());
        putAccountResponseV3.setBuyerCommission(mbxPutAccountResponseV3.getBuyerCommission());
        putAccountResponseV3.setSellerCommission(mbxPutAccountResponseV3.getSellerCommission());
        putAccountResponseV3.setCanTrade(mbxPutAccountResponseV3.isCanTrade());
        putAccountResponseV3.setCanWithdraw(mbxPutAccountResponseV3.isCanWithdraw());
        putAccountResponseV3.setCanDeposit(mbxPutAccountResponseV3.isCanDeposit());
        putAccountResponseV3.setAccountId(mbxPutAccountResponseV3.getAccountId());
        putAccountResponseV3.setExternalId(mbxPutAccountResponseV3.getExternalId());
        putAccountResponseV3.setExternalAccountId(mbxPutAccountResponseV3.getExternalAccountId());
        putAccountResponseV3.setUpdateTime(mbxPutAccountResponseV3.getUpdateTime());
        putAccountResponseV3.setUpdateId(mbxPutAccountResponseV3.getUpdateId());
        putAccountResponseV3.setAccountType(mbxPutAccountResponseV3.getAccountType());
        // restrictions
        MbxPutAccountResponseV3.Restrictions mbxRestrictions = mbxPutAccountResponseV3.getRestrictions();
        putAccountResponseV3.setRestrictions(convert(mbxRestrictions));
        // balances
        List<MbxPutAccountResponseV3.Balance> mbxBalances = mbxPutAccountResponseV3.getBalances();
        putAccountResponseV3.setBalances(convert(mbxBalances));
        putAccountResponseV3.setPermissions(mbxPutAccountResponseV3.getPermissions());
        return putAccountResponseV3;
    }

    private static List<PutAccountResponseV3.Balance> convert(List<MbxPutAccountResponseV3.Balance> mbxBalances) {
        if (mbxBalances != null) {
            List<PutAccountResponseV3.Balance> balanceList = Lists.newArrayList();
            for (MbxPutAccountResponseV3.Balance balance : mbxBalances) {
                balanceList.add(convert(balance));
            }
            return balanceList;
        } else {
            return Collections.emptyList();
        }
    }

    private static PutAccountResponseV3.Balance convert(MbxPutAccountResponseV3.Balance mbxBalance) {
        PutAccountResponseV3.Balance balance = new PutAccountResponseV3.Balance();
        balance.setAsset(mbxBalance.getAsset());
        balance.setFree(mbxBalance.getFree());
        balance.setLocked(mbxBalance.getLocked());
        balance.setExtLocked(mbxBalance.getExtLocked());
        return balance;
    }

    private static PutAccountResponseV3.Restrictions convert(MbxPutAccountResponseV3.Restrictions mbxRestrictions) {
        PutAccountResponseV3.Restrictions restrictions = new PutAccountResponseV3.Restrictions();
        restrictions.setRestrictionId(mbxRestrictions.getRestrictionId());
        restrictions.setMode(mbxRestrictions.getMode());
        restrictions.setSymbols(mbxRestrictions.getSymbols());
        return restrictions;
    }
}
