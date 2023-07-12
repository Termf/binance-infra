package com.binance.platform.mbx.model.userstream;

import com.binance.master.commons.ToString;

public class PingSteamResponse extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = 3202048771555071746L;

    private String listenKey;

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }
}
