package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetApiCheckRequest extends ToString {

    private static final long serialVersionUID = -948336696334438183L;

    public GetApiCheckRequest(@NotEmpty String apiKey, long userId, @NotEmpty String ip) {
        this.apiKey = apiKey;
        this.userId = userId;
        this.ip = ip;
    }

    @NotEmpty
    private String apiKey;
    private long userId;
    @NotEmpty
    private String ip;
    private String payload;
    private Long recvWindow;
    private Long timestamp;
    private String signature;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}