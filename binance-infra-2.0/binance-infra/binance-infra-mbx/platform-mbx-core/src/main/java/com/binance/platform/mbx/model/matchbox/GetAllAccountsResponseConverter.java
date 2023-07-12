package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllAccountsResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:02 上午
 */
public class GetAllAccountsResponseConverter {
    public static GetAllAccountsResponse convert(MbxGetAllAccountsResponse mbxGetAllAccountsResponse) {
        GetAllAccountsResponse getAllAccountsResponse = new GetAllAccountsResponse();
        getAllAccountsResponse.setAccountId(mbxGetAllAccountsResponse.getAccountId());
        getAllAccountsResponse.setExternalId(mbxGetAllAccountsResponse.getExternalId());
        getAllAccountsResponse.setExternalAccountId(mbxGetAllAccountsResponse.getExternalAccountId());
        getAllAccountsResponse.setCanTrade(mbxGetAllAccountsResponse.isCanTrade());
        getAllAccountsResponse.setCanWithdraw(mbxGetAllAccountsResponse.isCanWithdraw());
        getAllAccountsResponse.setCanDeposit(mbxGetAllAccountsResponse.isCanDeposit());
        getAllAccountsResponse.setAccountType(mbxGetAllAccountsResponse.getAccountType());
        return getAllAccountsResponse;
    }
}
