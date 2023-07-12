package com.binance.platform.sapi.degrade;

/**
 * @author 陈添明
 */
public class RetrofitBlockException extends RuntimeException {

    public RetrofitBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrofitBlockException(Throwable cause) {
        super(cause);
    }
}
