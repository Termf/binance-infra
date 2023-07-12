package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeyWhitelistResult;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 2:58 上午
 */
public class GetApiKeyWhitelistResponseConverter {
    public static GetApiKeyWhitelistResponse convert(MbxApiKeyWhitelistResult mbxApiKeyWhitelistResult) {
        GetApiKeyWhitelistResponse getApiKeyWhitelistResponse = new GetApiKeyWhitelistResponse();
        getApiKeyWhitelistResponse.setKeyId(mbxApiKeyWhitelistResult.getKeyId());
        getApiKeyWhitelistResponse.setAccountId(mbxApiKeyWhitelistResult.getAccountId());
        getApiKeyWhitelistResponse.setSymbols(mbxApiKeyWhitelistResult.getSymbols());
        getApiKeyWhitelistResponse.setLastUpdateTime(mbxApiKeyWhitelistResult.getLastUpdateTime());
        return getApiKeyWhitelistResponse;
    }
}
