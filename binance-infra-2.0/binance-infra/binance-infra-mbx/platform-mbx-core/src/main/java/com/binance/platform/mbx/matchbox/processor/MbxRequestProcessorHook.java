package com.binance.platform.mbx.matchbox.processor;

import com.binance.master.error.GeneralCode;
import com.binance.master.utils.StringUtils;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.MbxState;
import com.binance.platform.mbx.monitor.MonitorUtil;
import com.binance.platform.mbx.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Hook interface, used to override some default action of the {@link MbxRequestProcessor}.
 *
 * @param <T>
 * @param <U>
 */
public interface MbxRequestProcessorHook<T extends MbxBaseRequest, U> {
    Logger LOGGER = LoggerFactory.getLogger(MbxRequestProcessorHook.class);
    /**
     * The prefix of the array: {@value}
     */
    String ARRAY_PREFIX = "[";
    /**
     * The success response of the single object: {@value}
     */
    String EMPTY_SUCCESS_RESPONSE = "{}";

    /**
     * Request check in addition to validator.
     *
     * @param request
     * @throws MbxException if the request is invalid, throw the exception，whose state is MbxState#InvalidParam
     */
    default void checkParam(T request) throws MbxException {

    }

    /**
     * Attach some additional params for calling the match box.
     *
     * @param request
     * @return
     */
    default Map<String, List<String>> prepareAdditionalParams(T request) {
        return Collections.emptyMap();
    }

    /**
     * Deserialization for the json response
     *
     * @param jsonResponse
     * @return
     */
    default MbxResponse<U> postProcess(HttpMethod method, T request, String jsonResponse, TypeReference<U> typeReference) throws MbxException {
        MbxResponse mbxResponse = new MbxResponse();
        try {
            // check error from match box
            if (StringUtils.startsWith(jsonResponse, ARRAY_PREFIX) || Objects.equals(jsonResponse, EMPTY_SUCCESS_RESPONSE)) {
                mbxResponse.setState(new MbxState());
            } else {
                MbxState mbxState = JsonUtil.fromJson(jsonResponse, MbxState.class);
                mbxResponse.setState(mbxState);
            }

            Type type = typeReference.getType();

            // get result data
            if (type != Void.class && mbxResponse.isSuccess()) {
                if (type == String.class) {
                    mbxResponse.setData((U) jsonResponse);
                } else {
                    U u = JsonUtil.fromJson(jsonResponse, typeReference);
                    mbxResponse.setData(u);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Deserialize response error", e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }
        return mbxResponse;
    }
}
