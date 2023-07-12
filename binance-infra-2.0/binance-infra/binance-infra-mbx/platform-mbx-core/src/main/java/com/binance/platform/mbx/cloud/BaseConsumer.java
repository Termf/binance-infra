package com.binance.platform.mbx.cloud;

import com.binance.master.enums.LanguageEnum;
import com.binance.master.enums.TerminalEnum;
import com.binance.master.models.APIRequest;
import com.binance.platform.common.TrackingUtils;

/**
 * @author Li Fenggang
 * Date: 2020/7/8 7:10 下午
 */
public class BaseConsumer {

    /**
     * Common builder
     *
     * @param userIdRequest
     * @return
     */
    protected <T> APIRequest<T> buildApiRequest(T userIdRequest) {
        APIRequest<T> request = new APIRequest<>();
        request.setBody(userIdRequest);
        request.setLanguage(LanguageEnum.EN_US);
        request.setTerminal(TerminalEnum.WEB);
        request.setTrackingChain(TrackingUtils.getTrace());
        return request;
    }

}
