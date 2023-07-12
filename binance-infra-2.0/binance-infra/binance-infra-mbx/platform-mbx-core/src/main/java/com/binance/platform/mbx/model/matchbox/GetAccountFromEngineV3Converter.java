package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Response;

import java.util.ArrayList;
import java.util.List;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/3/1
 */
public class GetAccountFromEngineV3Converter {
    public static MbxGetAccountFromEngineV3Request convertRequest(GetAccountFromEngineV3Request request) {
        MbxGetAccountFromEngineV3Request mbxGetAccountFromEngineV3Request = new MbxGetAccountFromEngineV3Request(request.getAccountId());
        return mbxGetAccountFromEngineV3Request;
    }

    public static GetAccountFromEngineV3Response convertResponse(MbxGetAccountFromEngineV3Response response) {
        GetAccountFromEngineV3Response getAccountFromEngineV3Response = new GetAccountFromEngineV3Response();
        getAccountFromEngineV3Response.setMakerCommission(response.getMakerCommission());
        getAccountFromEngineV3Response.setTakerCommission(response.getTakerCommission());
        getAccountFromEngineV3Response.setBuyerCommission(response.getBuyerCommission());
        getAccountFromEngineV3Response.setSellerCommission(response.getSellerCommission());
        getAccountFromEngineV3Response.setCanTrade(response.isCanTrade());
        getAccountFromEngineV3Response.setCanWithdraw(response.isCanWithdraw());
        getAccountFromEngineV3Response.setCanDeposit(response.isCanDeposit());
        getAccountFromEngineV3Response.setAccountId(response.getAccountId());
        getAccountFromEngineV3Response.setExternalId(response.getExternalId());
        getAccountFromEngineV3Response.setExternalAccountId(response.getExternalAccountId());
        getAccountFromEngineV3Response.setUpdateTime(response.getUpdateTime());
        getAccountFromEngineV3Response.setUpdateId(response.getUpdateId());
        getAccountFromEngineV3Response.setAccountType(response.getAccountType());
        getAccountFromEngineV3Response.setRestrictions(convert(response.getRestrictions()));
        // balance
        List<MbxGetAccountFromEngineV3Response.Balance> mbxBalances = response.getBalances();
        List<GetAccountFromEngineV3Response.Balance> balanceList = new ArrayList<>();
        for (MbxGetAccountFromEngineV3Response.Balance mbxBalance : mbxBalances) {
            balanceList.add(convert(mbxBalance));
        }
        getAccountFromEngineV3Response.setBalances(balanceList);
        getAccountFromEngineV3Response.setPermissions(response.getPermissions());
        return getAccountFromEngineV3Response;
    }

    private static GetAccountFromEngineV3Response.Balance convert(MbxGetAccountFromEngineV3Response.Balance mbxBalance) {
        GetAccountFromEngineV3Response.Balance balance = new GetAccountFromEngineV3Response.Balance();
        balance.setAsset(mbxBalance.getAsset());
        balance.setFree(mbxBalance.getFree());
        balance.setLocked(mbxBalance.getLocked());
        balance.setExtLocked(mbxBalance.getExtLocked());
        return balance;
    }

    private static GetAccountFromEngineV3Response.Restrictions convert(MbxGetAccountFromEngineV3Response.Restrictions mbxRestrictions) {
        GetAccountFromEngineV3Response.Restrictions restrictions = new GetAccountFromEngineV3Response.Restrictions();
        restrictions.setRestrictionId(mbxRestrictions.getRestrictionId());
        restrictions.setMode(mbxRestrictions.getMode());
        restrictions.setSymbols(mbxRestrictions.getSymbols());
        return restrictions;
    }
}
