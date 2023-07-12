package com.binance.platform.sapi.config;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
public class MethodMetaData {
    private boolean needSign = false;
    private String method;
    private BodyMetaData body;
    private String uri;

    public boolean isNeedSign() {
        return needSign;
    }

    public void setNeedSign(boolean needSign) {
        this.needSign = needSign;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BodyMetaData getBody() {
        return body;
    }

    public void setBody(BodyMetaData body) {
        this.body = body;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}