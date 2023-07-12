package com.binance.matchbox.support;

import com.alibaba.fastjson.JSONObject;
import com.binance.master.error.BusinessException;
import com.binance.master.error.GeneralCode;
import com.binance.matchbox.vo.MatchboxErrorResponse;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Fei.Huang on 2018/5/30.
 */
@Slf4j
public class MatchBoxErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        MatchboxErrorResponse errorResponse = null;
        try {
            Response.Body body = response.body();
            String content = Util.toString(body.asReader());
            errorResponse = JSONObject.toJavaObject(JSONObject.parseObject(content), MatchboxErrorResponse.class);
        } catch (Exception e) {
            log.debug("errorResponse not match MatchboxErrorResponse");
        }
        if (null != errorResponse) {
            String errorMsg = String.format("code:%s, msg:%s", errorResponse.getCode(), errorResponse.getMsg());
            log.error("Call matching box error, {}", errorMsg);
            throw new BusinessException(GeneralCode.SYS_ERROR, errorMsg);
        }

        ErrorDecoder errorDecoder = new Default();
        return errorDecoder.decode(methodKey, response);
    }

}
