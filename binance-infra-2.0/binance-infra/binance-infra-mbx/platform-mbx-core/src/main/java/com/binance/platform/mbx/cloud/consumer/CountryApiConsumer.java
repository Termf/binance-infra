package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:18 下午
 */
public class CountryApiConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public CountryApiConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    /**
     * 判断用户是否在黑名单中（根据国家userId）
     *
     * @param userId
     * @return
     */
    public APIResponse<Boolean> isUserInBlacklist(long userId) {
        Map<String, List<String>> paramMap = new HashMap<>();
        List<String> userIdList = Arrays.asList(String.valueOf(userId));
        paramMap.put("userId", userIdList);
        APIResponse<Boolean> response = cloudApiCaller.get(ApiConstants.SERVICE_ID_ACCOUNT, "/country/blacklist/check/userId", paramMap,
                new TypeReference<APIResponse<Boolean>>() {});
        return response;
    }
}
