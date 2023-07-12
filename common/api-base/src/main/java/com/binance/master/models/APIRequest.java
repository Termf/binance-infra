package com.binance.master.models;

import java.io.Serializable;

import javax.validation.Valid;

import com.binance.master.error.BusinessException;
import com.binance.master.error.GeneralCode;

import io.swagger.annotations.ApiModelProperty;

/**
 * 统一请求类
 *
 * @param <T>
 * @author wang
 */
public class APIRequest<T> extends APIRequestHeader {
    /**
     *
     */
    private static final long serialVersionUID = -1385085874260105799L;

    @ApiModelProperty(value = "业务请求(Get请求忽略)")
    @Valid
    private T body;

    public static <T> APIRequest<T> instance(APIRequestHeader originRequest, T body) {
        APIRequest<T> request = new APIRequest<>();
        request.setLanguage(originRequest.getLanguage());
        request.setTerminal(originRequest.getTerminal());
        request.setToken(originRequest.getToken());
        request.setVersion(originRequest.getVersion());
        request.setBody(body);
        return request;
    }

    public static <T> APIRequest<T> instance(T body) {
        return instance(APIRequestHeader.HEADER_DEFAULT, body);
    }


    public static <T> APIRequest<T> instance(APIRequestHeader originRequest, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        return instance(originRequest, clazz.newInstance());
    }

    public static <T> APIRequest<T> instance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return instance(APIRequestHeader.HEADER_DEFAULT, clazz.newInstance());
    }

    public static APIRequest<VoidBody> instanceBodyNull(APIRequestHeader originRequest)
            throws InstantiationException, IllegalAccessException {
        return instance(originRequest, new VoidBody());
    }

    public static APIRequest<VoidBody> instanceBodyNull() {
        return instance(APIRequestHeader.HEADER_DEFAULT, new VoidBody());
    }

    /**
     * @return Returns the reqeust.
     */
    public T getBody() {
        if (body == null) {
            throw new BusinessException(GeneralCode.SYS_BODY_NULL);
        }
        return body;
    }

    /**
     * 根据类型获取Body
     *
     * @param bizRequestClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    public T getBody(Class bodyClass) {
        if (body != null && body.getClass().equals(bodyClass)) {
            return body;
        }
        return null;
    }

    /**
     * @param Body The Body to set.
     */
    public void setBody(T body) {
        this.body = body;
    }

    public static class VoidBody implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 5709827601289699488L;
    }
}
