package com.binance.platform.openfeign.gray;

/**
 * 透传HTTP Header扩展
 */
public interface PenetrateHttpHeaderHandler {

    /**
     * 是否需要透传
     * 
     * @param headerKey
     * @return true 代表需要透传
     */
    public boolean needPenetrateHeader(String headerKey);

    /**
     * 覆盖Http Header
     * 
     * @param headerKey
     * @param headerValue
     * @return
     */
    default String replaceHeader(String headerKey, String headerValue) {
        return headerValue;
    }

}
