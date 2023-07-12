package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyResponse;

/**
 * @author Li Fenggang
 * Date: 2020/7/19 8:57 上午
 */
public class PostApiKeyResponseConverter {
    public static PostApiKeyResponse convert(MbxPostApiKeyResponse source) {
        PostApiKeyResponse postApiKeyResponse = new PostApiKeyResponse();
        postApiKeyResponse.setApiKey(source.getApiKey());
        postApiKeyResponse.setSecretKey(source.getSecretKey());
        postApiKeyResponse.setKeyId(source.getKeyId());
        postApiKeyResponse.setType(source.getType());
        return postApiKeyResponse;

    }
}
