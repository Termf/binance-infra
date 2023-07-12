package com.binance.platform.mbx.matchbox.model;

import com.binance.platform.mbx.util.JsonUtil;

/**
 * Base model
 *
 * @author Li Fenggang
 * Date: 2020/7/2 11:11 上午
 */
public class MbxBaseModel {
    @Override
    public String toString() {
        String json = JsonUtil.toJsonStringSilently(this);
        return json;
    }
}
