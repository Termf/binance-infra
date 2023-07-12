package com.binance.master.error;

public interface ErrorCode {
    /**
     * 获取异常code
     *
     * @return
     */
    public String getCode();

    /**
     * 获取异常描述信息
     *
     * @return
     */
    public String getMessage();
}
