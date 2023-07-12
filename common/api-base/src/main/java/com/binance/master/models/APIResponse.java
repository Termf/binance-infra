package com.binance.master.models;

import com.binance.master.commons.ToString;
import com.binance.master.error.GeneralCode;

public class APIResponse<T> extends ToString {

    private static final long serialVersionUID = 3860117858927336896L;

    public static enum Status {
        // 正常
        OK,
        // 异常
        ERROR
    }

    public static enum Type {
        // 系统
        SYS,
        // 一般
        GENERAL,
        // 验证
        VALID
    }

    public static APIResponse<?> OK = new APIResponse<>(Status.OK, Type.GENERAL, GeneralCode.SUCCESS.getCode());

    private Status status;

    private Type type = Type.GENERAL;

    private String code;

    private Object errorData;

    private T data;

    private Object subData;// 错误异常时辅助返回字段
    // 错误码中会动态变化的参数，例如password error, you have {0} tries left.
    private Object[] params;

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return this.code;
    }

    public T getData() {
        return data;
    }

    public Object getErrorData() {
        return errorData;
    }

    public Object getSubData() {
        return subData;
    }

    public Object[] getParams() {
        return params;
    }

    public APIResponse() {
        super();
    }

    public APIResponse(Status status, String code) {
        super();
        this.status = status;
        this.code = code;
    }

    public APIResponse(Status status, Type type, String code) {
        super();
        this.status = status;
        this.type = type;
        this.code = code;
    }

    public APIResponse(Status status, String code, T data) {
        super();
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public APIResponse(Status status, Type type, String code, T data) {
        super();
        this.status = status;
        this.type = type;
        this.code = code;
        this.data = data;
    }

    public APIResponse(Status status, Type type, String code, Object errorData, T data) {
        super();
        this.status = status;
        this.type = type;
        this.code = code;
        this.errorData = errorData;
        this.data = data;
    }

    public APIResponse(Status status, Type type, String code, Object errorData, T data, Object subData) {
        super();
        this.status = status;
        this.type = type;
        this.code = code;
        this.errorData = errorData;
        this.data = data;
        this.subData = subData;
    }

    public APIResponse(Status status, Type type, String code, Object errorData, T data, Object[] params,
            Object subData) {
        super();
        this.status = status;
        this.type = type;
        this.code = code;
        this.errorData = errorData;
        this.data = data;
        this.params = params;
        this.subData = subData;
    }

    public APIResponse(Status status, Type type, Object errorData) {
        super();
        this.status = status;
        this.type = type;
        this.errorData = errorData;
    }

    public static <T> APIResponse<T> getOKJsonResult() {
        APIResponse<T> result =
                new APIResponse<>(APIResponse.Status.OK, APIResponse.Type.GENERAL, GeneralCode.SUCCESS.getCode(), null);
        return result;
    }

    public static <T> APIResponse<T> getOKJsonResult(T data) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.OK, APIResponse.Type.GENERAL,
                GeneralCode.SUCCESS.getCode(), data);
        return result;
    }

    public static <T> APIResponse<T> getOKJsonResult(T data, Object subData) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.OK, APIResponse.Type.GENERAL,
                GeneralCode.SUCCESS.getCode(), data);
        result.subData = subData;
        return result;
    }

    public static <T> APIResponse<T> getErrorJsonResult(Object errorData) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.ERROR, Type.GENERAL,
                GeneralCode.SYS_VALID.getCode(), errorData, null);
        return result;
    }

    public static <T> APIResponse<T> getErrorJsonResult(Type type, String code, Object errorData) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.ERROR, type, code, errorData, null);
        return result;
    }

    public static <T> APIResponse<T> getErrorJsonResult(Type type, String code, Object errorData, Object subData) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.ERROR, type, code, errorData, null, subData);
        return result;
    }

    public static <T> APIResponse<T> getErrorJsonResult(Type type, String code, Object errorData, Object[] params,
            Object subData) {
        APIResponse<T> result =
                new APIResponse<T>(APIResponse.Status.ERROR, type, code, errorData, null, params, subData);
        return result;
    }

    public static <T> APIResponse<T> getErrorJsonResult(Type type, Object errorData) {
        APIResponse<T> result = new APIResponse<T>(APIResponse.Status.ERROR, type, errorData);
        return result;
    }

}
