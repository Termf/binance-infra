package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.constant.CustomHeaderConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 10:51 上午
 */
public class MbxGetApiKeyCheckRequestBuilder {
    public static MbxGetApiKeyCheckRequest build(String ip, String payload, Long recvWindow, Long timestamp, String signature, String apiKey) {
        MbxGetApiKeyCheckRequest request = new MbxGetApiKeyCheckRequest(ip);
        request.setPayload(payload);
        request.setRecvWindow(recvWindow);
        request.setTimestamp(timestamp);
        request.setSignature(signature);

        Map<String, String> headerMap = new HashMap<>(1, 1);
        headerMap.put(CustomHeaderConstant.HEADER_MBX_API_KEY, apiKey);
        request.setHeaderMap(headerMap);

        return request;
    }
}
