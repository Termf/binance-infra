package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.model.SysConfigResp;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:18 下午
 */
public class BaseDataConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public BaseDataConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    public List<SysConfigResp> getAllSysConfig() {
        APIResponse<List<SysConfigResp>> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_BASS_DATA, "/sysconf/getAllSysConfig", null,
                new TypeReference<APIResponse<List<SysConfigResp>>>() {
                });
        List<SysConfigResp> configRespList = ApiResponseUtil.getAPIRequestResponse(response);
        return configRespList;
    }
}
