package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetApiKeyCheckRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String apiKey;
    @NotEmpty
    private String ip;
    private String payload;
    private Long recvWindow;
    private String signature;
    private Long timestamp;

    public GetApiKeyCheckRequest(@NotEmpty String apiKey, @NotEmpty String ip) {
        this.apiKey = apiKey;
        this.ip = ip;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Long getRecvWindow() {
        return recvWindow;
    }

    public void setRecvWindow(Long recvWindow) {
        this.recvWindow = recvWindow;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * 签名是针对payload字段做的。
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}