package com.binance.platform.mbx.cloud.util;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.exception.MbxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 8:25 下午
 */
public class ApiResponseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseUtil.class);

    /**
     * 解析返回相应返回值，如果相应出错，则抛出MbxException
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> T getAPIRequestResponse(APIResponse<T> response) {
        if (APIResponse.Status.OK.equals(response.getStatus())) {
            return response.getData();
        } else {
            Object errorData = response.getErrorData();
            if (errorData == null) {
                errorData = response.getSubData();
            }
            String responseCode = response.getCode();
            String errorMsg = errorData.toString();
            GeneralCode errorCode = GeneralCode.findByCode(responseCode);
            LOGGER.warn("response.code:{}, errorData:{}, resolved.code: {}", responseCode, errorMsg, errorCode);
            throw new MbxException(errorCode, errorMsg);
        }
    }
}
