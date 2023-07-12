package com.binance.platform.sapi.exception;

public class ErrorResult {
    public static final int DEFAULT_ERROR_CODE = -1000;
    public static final String DEFAULT_ERROR_MSG = "An unknown error occurred while processing the request.";

    private int code = DEFAULT_ERROR_CODE;
    private String msg = DEFAULT_ERROR_MSG;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}