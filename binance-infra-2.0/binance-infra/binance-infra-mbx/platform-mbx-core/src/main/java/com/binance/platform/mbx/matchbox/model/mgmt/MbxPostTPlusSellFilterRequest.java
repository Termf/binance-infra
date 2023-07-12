package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostTPlusSellFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/tPlusSellFilter";
    }

    private long endTime;
    @NotEmpty
    private String symbol;
    private String exemptAcct;

    public MbxPostTPlusSellFilterRequest(long endTime, @NotEmpty String symbol) {
        this.endTime = endTime;
        this.symbol = symbol;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExemptAcct() {
        return exemptAcct;
    }

    public void setExemptAcct(String exemptAcct) {
        this.exemptAcct = exemptAcct;
    }
}
