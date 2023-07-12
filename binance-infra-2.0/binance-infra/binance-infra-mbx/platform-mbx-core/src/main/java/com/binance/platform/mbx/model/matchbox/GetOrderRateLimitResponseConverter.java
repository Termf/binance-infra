package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderRateLimitResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 6:12 上午
 */
public class GetOrderRateLimitResponseConverter {

    public static GetOrderRateLimitResponse convert(MbxGetOrderRateLimitResponse mbxGetOrderRateLimitResponse) {
        GetOrderRateLimitResponse getOrderRateLimitResponse = new GetOrderRateLimitResponse();
        getOrderRateLimitResponse.setRateInterval(mbxGetOrderRateLimitResponse.getRateInterval());
        getOrderRateLimitResponse.setIntervalNum(mbxGetOrderRateLimitResponse.getIntervalNum());
        getOrderRateLimitResponse.setRateLimit(mbxGetOrderRateLimitResponse.getRateLimit());
        List<MbxGetOrderRateLimitResponse.Override> mbxOverrides = mbxGetOrderRateLimitResponse.getOverrides();
        if (mbxOverrides != null) {
            List<GetOrderRateLimitResponse.Override> overrides = new ArrayList<>(mbxOverrides.size());
            for (MbxGetOrderRateLimitResponse.Override mbxOverride : mbxOverrides) {
                overrides.add(convert(mbxOverride));
            }
            getOrderRateLimitResponse.setOverrides(overrides);
        } else {
            getOrderRateLimitResponse.setOverrides(Collections.emptyList());
        }
        return getOrderRateLimitResponse;
    }

    private static GetOrderRateLimitResponse.Override convert(MbxGetOrderRateLimitResponse.Override mbxOverride) {
        GetOrderRateLimitResponse.Override override = new GetOrderRateLimitResponse.Override();
        override.setAccountId(mbxOverride.getAccountId());
        override.setLimit(mbxOverride.getLimit());
        return override;


    }
}
