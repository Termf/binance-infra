package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIRequest;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.model.GetUserResponse;
import com.binance.platform.mbx.cloud.model.UserIdRequest;
import com.binance.platform.mbx.cloud.model.UserStatusEx;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.exception.MbxException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Li Fenggang
 * Date: 2020/7/8 6:07 下午
 */
public class UserApiConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public UserApiConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    public APIResponse<GetUserResponse> getUserById(long userId) throws MbxException {
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(userId);

        APIRequest apiRequest = buildApiRequest(userIdRequest);

        APIResponse<GetUserResponse> getUserResponse = cloudApiCaller.post(ApiConstants.SERVICE_ID_ACCOUNT, "/user/getUserById",
                apiRequest, new TypeReference<APIResponse<GetUserResponse>>() {});

        return getUserResponse;
    }

    public APIResponse<UserStatusEx> getUserStatusByUserId(long userId) throws MbxException {
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(userId);

        APIRequest apiRequest = buildApiRequest(userIdRequest);

        APIResponse<UserStatusEx> accountResponse = cloudApiCaller.post(ApiConstants.SERVICE_ID_ACCOUNT, "/user/status",
                apiRequest, new TypeReference<APIResponse<UserStatusEx>>() {});

        return accountResponse;
    }
}
