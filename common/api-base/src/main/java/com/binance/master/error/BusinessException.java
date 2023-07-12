package com.binance.master.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5886588821403377426L;
    private ErrorCode errorCode;
    /**当无法使用ErrorCode进行异常抛出的时候，可以通过构造bizCode bizMessage进行异常抛出*/
    private String bizCode;
    private String bizMessage;

    private Object[] params;

    /**
     * 错误异常时辅助返回字段
     */
    private Object subData;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getParams() {
        return params;
    }

    public Object getSubData() {
        return subData;
    }
    // 不建议使用，要定义明确的错误码，方便问题排查
    @Deprecated()
    public BusinessException(String msg) {
        super(msg);
        this.subData = msg;
        this.errorCode = GeneralCode.COMMON_ERROR;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getCode() + ":" + errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Object[] params) {
        this(errorCode);
//        this.errorCode = errorCode;
        this.params = params;
    }

    public BusinessException(ErrorCode errorCode, Object subData, Object[] params) {
        this(errorCode);
//        this.errorCode = errorCode;
        this.subData = subData;
        this.params = params;
    }

    public BusinessException(String  bizCode,String bizMessage, Object... params) {
        super(bizCode+"="+bizMessage);
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
        this.params = params;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizMessage() {
        return bizMessage;
    }

    public void setBizMessage(String bizMessage) {
        this.bizMessage = bizMessage;
    }

    @Override
    public String toString() {
        if(this.errorCode == null){
            return String.format("bizCode:%s,bizMessage:%s", this.bizCode, this.bizMessage);
        }
        return String.format("errorCode:%s,msg:%s", this.errorCode.getCode(), this.errorCode.getMessage());
    }

}
