package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MgmtGetTimeResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 1:56 下午
 */
public class GetTimeResponseConverter {
    public static GetTimeResponse convert(MgmtGetTimeResponse mgmtGetTimeResponse) {
        GetTimeResponse getTimeResponse = new GetTimeResponse();
        getTimeResponse.setServerTime(mgmtGetTimeResponse.getServerTime());
        return getTimeResponse;
    }
}
