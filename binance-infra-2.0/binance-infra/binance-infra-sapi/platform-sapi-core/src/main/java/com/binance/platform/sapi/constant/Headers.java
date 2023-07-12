package com.binance.platform.sapi.constant;

/**
 * 请求头
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
public interface Headers {
    /**
     * SAPI接收的apiKey的header名称
     */
    String API_KEY = "X-MBX-APIKEY";
    /**
     * secretKey占位符，用于客户单在header中识别secretKey
     */
    String SECURITY_KEY = "sapi_client_security_key";
}
