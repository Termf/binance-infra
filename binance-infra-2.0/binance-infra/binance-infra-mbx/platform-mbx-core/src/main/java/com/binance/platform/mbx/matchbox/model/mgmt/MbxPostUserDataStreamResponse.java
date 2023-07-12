package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostUserDataStreamResponse extends MbxBaseModel {

    /**
     * listenKey : OyrSVK6frlI3xMMggLHyZKpoXme061HLimZ8dGRd3Jt9V0ntJVXQpGtQXxw4
     */

    private String listenKey;

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }
}
