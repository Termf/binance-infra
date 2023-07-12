package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:30 上午
 */
public class GetSymbolHistoryResponse extends ToString {

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
