package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:37 上午
 */
public class MbxGetSymbolHistoryResponse extends MbxBaseModel {
    /**
     * status : TRADING
     * time : 1594778387242
     */

    private String status;
    private Long time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
