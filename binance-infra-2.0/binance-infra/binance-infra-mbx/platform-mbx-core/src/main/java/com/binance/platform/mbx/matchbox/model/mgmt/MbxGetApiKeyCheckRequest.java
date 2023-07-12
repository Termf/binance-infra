package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 10:51 上午
 */
public class MbxGetApiKeyCheckRequest extends MbxBaseRequest {
    @NotNull
    private String ip;
    private String payload;
    private Long recvWindow;
    private Long timestamp;
    private String signature;

    public MbxGetApiKeyCheckRequest(@NotNull String ip) {
        this.ip = ip;
    }

    @Override
    public String getUri() {
        return "/v1/apiKey/check";
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
