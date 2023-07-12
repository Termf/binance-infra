package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIRequest;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.model.QueryOpenOrderRequest;
import com.binance.platform.mbx.cloud.model.QueryOpenOrderResponse;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:18 下午
 */
public class OrderApiConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public OrderApiConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    public APIResponse<QueryOpenOrderResponse> queryOpenOrder(QueryOpenOrderRequest request) {
        APIRequest apiRequest = buildApiRequest(request);
        APIResponse<QueryOpenOrderResponse> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_STREAMER_WEB, "/order/openOrder", apiRequest,
                new TypeReference<APIResponse<QueryOpenOrderResponse>>() {
                });
        return response;
    }
}
