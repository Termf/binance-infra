package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import java.util.Map;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostBalanceBatchRequest extends MbxBaseRequest {

    public MbxPostBalanceBatchRequest(Map<String, String> formData) {
        setFormData(formData);
    }

    @Override
    public String getUri() {
        return "/v1/balance/batch";
    }
}
