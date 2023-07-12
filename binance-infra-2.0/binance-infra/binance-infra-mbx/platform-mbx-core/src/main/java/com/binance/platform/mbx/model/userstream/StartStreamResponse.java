package com.binance.platform.mbx.model.userstream;

import com.binance.master.commons.ToString;

public class StartStreamResponse extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = -8906372444617970398L;

    private String listenKey;

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }
}
