package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIRequest;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.cloud.model.UserIdRequest;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:18 下午
 */
public class SubAccountApiConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public SubAccountApiConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    /**
     * 非子账户，或已被母账户启用的子账户
     *
     * @param request
     * @return
     */
    public APIResponse<Boolean> notSubUserOrIsEnabledSubUser(UserIdRequest request) {
        APIRequest apiRequest = buildApiRequest(request);
        APIResponse<Boolean> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ACCOUNT, "/sub-user/status/check/non-sub-user/enabled-sub-user", apiRequest,
                new TypeReference<APIResponse<Boolean>>() {
                });
        return response;
    }
}
