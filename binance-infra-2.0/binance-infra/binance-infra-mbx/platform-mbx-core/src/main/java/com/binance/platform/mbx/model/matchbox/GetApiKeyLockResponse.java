package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:11 上午
 */
public class GetApiKeyLockResponse extends ToString {

    /**
     * isLocked : false
     */

    private Boolean isLocked;

    public Boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
}
