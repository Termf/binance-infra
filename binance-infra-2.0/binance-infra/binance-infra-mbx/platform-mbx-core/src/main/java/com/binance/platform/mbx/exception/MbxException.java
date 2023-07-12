package com.binance.platform.mbx.exception;

import com.binance.master.error.BusinessException;
import com.binance.master.error.ErrorCode;

/**
 * 异常基类
 *
 * @author Li Fenggang
 * Date: 2020/6/30 4:31 下午
 */
public class MbxException extends BusinessException {
    @Deprecated
    public MbxException(String msg) {
        super(msg);
    }

    public MbxException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MbxException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MbxException(ErrorCode errorCode, Object[] params) {
        super(errorCode, params);
    }

    public MbxException(ErrorCode errorCode, Object subData, Object[] params) {
        super(errorCode, subData, params);
    }

    public MbxException(String bizCode, String bizMessage, Object... params) {
        super(bizCode, bizMessage, params);
    }
}
