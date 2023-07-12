package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckRequestBuilder;

public class GetApiKeyCheckRequestConverter {
    public static MbxGetApiKeyCheckRequest convert(GetApiKeyCheckRequest source) {
        MbxGetApiKeyCheckRequest request = MbxGetApiKeyCheckRequestBuilder.build(source.getIp(), source.getPayload(), source.getRecvWindow(),
                source.getTimestamp(), source.getSignature(), source.getApiKey());

        return request;
    }
}