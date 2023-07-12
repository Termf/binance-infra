package com.binance.platform.mbx.matchbox.rpc;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * Match box请求发送器
 */
public interface MbxCaller {
    /**
     * 发送请求到match box
     *
     * @param rootUrl
     * @param httpMethod
     * @param request
     * @param additionalParamMap
     * @return
     */
    String send(String rootUrl, HttpMethod httpMethod, MbxBaseRequest request, Map<String, List<String>> additionalParamMap) throws MbxException;
}
