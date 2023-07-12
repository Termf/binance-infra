package com.binance.platform.mbx.cloud.rpc;

import com.binance.platform.mbx.exception.MbxException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * service caller
 */
public interface CloudApiCaller {
    /**
     * post request
     *
     * @param serviceId
     * @param path
     * @param request
     * @param typeReference
     * @param <T>
     * @param <U>
     * @return
     */
    <T, U> U post(String serviceId, String path, T request, TypeReference<U> typeReference) throws MbxException;

    /**
     * post request
     *
     * @param serviceId
     * @param path
     * @param request
     * @param <T>
     * @param <U>
     * @return
     */
    <T, U> U post(String serviceId, String path, Map<String, List<String>> queryParams, T request, TypeReference<U> typeReference) throws MbxException;

    /**
     * get request
     *
     * @param serviceId
     * @param path
     * @param typeReference
     * @param <T>
     * @return
     */
    <T> T get(String serviceId, String path, TypeReference<T> typeReference) throws MbxException;

    /**
     * get request
     *
     * @param serviceId
     * @param path
     * @param queryParams
     * @param typeReference
     * @param <T>
     * @return
     */
    <T> T get(String serviceId, String path, Map<String, List<String>> queryParams, TypeReference<T> typeReference) throws MbxException;
}
