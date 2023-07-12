package com.binance.platform.mbx.matchbox.processor;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.rpc.MbxCaller;
import com.binance.platform.mbx.util.ValidationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/1 10:54 上午
 */
public class MbxRequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MbxRequestProcessor.class);

    private final MbxRequestProcessorHook DEFAULT_HOOK = new MbxRequestProcessorHook(){};

    private final MbxCaller mbxCaller;

    public MbxRequestProcessor(MbxCaller mbxCaller) {
        this.mbxCaller = mbxCaller;
    }

    public <T extends MbxBaseRequest, U>  MbxResponse<U> process(String rootUrl,HttpMethod method, T request,
                                                                 TypeReference<U> typeReference) throws MbxException {
        return process(rootUrl, method, request, typeReference, null);
    }

    public <T extends MbxBaseRequest, U> MbxResponse<U> process(String rootUrl, HttpMethod method, T request,
                                                                TypeReference<U> typeReference,
                                                                MbxRequestProcessorHook<T, U> processorHook) throws MbxException {
        long startNs = System.nanoTime();
        Exception ex = null;
        MbxResponse<U> response = null;
        LOGGER.debug("http.method:{}, request:{}", method, request);
        try {
            if (processorHook == null) {
                processorHook = DEFAULT_HOOK;
            }

            // 参数校验
            // put the entrance of the sdk
//            validCheck(request);

            processorHook.checkParam(request);

            // 参数补充
            Map<String, List<String>> additionalParamMap = processorHook.prepareAdditionalParams(request);

            // 发送请求
            String jsonResult = mbxCaller.send(rootUrl, method, request, additionalParamMap);

            // 调用后处理
            response = processorHook.postProcess(method, request, jsonResult, typeReference);
            return response;
        } catch(MbxException e) {
            ex = e;
            throw e;
        }  catch(Exception e) {
            LOGGER.error("Invoking match box error", e);
            ex = e;
            throw new MbxException(GeneralCode.SYS_ERROR);
        } finally {
            // 计时
            long elapseNs = System.nanoTime() - startNs;
            long elapseMs = TimeUnit.NANOSECONDS.toMillis(elapseNs);
            LOGGER.debug("takes [{} ms], response:{}", elapseMs, response);
        }
    }

    /**
     * 面向注解的Validator校验
     *
     * @param requestParam
     * @throws MbxException
     */
    private final void validCheck(Object requestParam) throws MbxException {
        ValidationUtil.validCheck(requestParam);
    }

}
