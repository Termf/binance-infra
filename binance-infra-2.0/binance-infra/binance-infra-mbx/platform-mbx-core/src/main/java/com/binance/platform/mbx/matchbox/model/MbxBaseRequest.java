package com.binance.platform.mbx.matchbox.model;

import com.binance.platform.mbx.matchbox.annotation.MbxIgnored;

import java.util.Collections;
import java.util.Map;

/**
 * 请求的基础类
 *
 * @author Li Fenggang
 * Date: 2020/7/2 11:11 上午
 */
public abstract class MbxBaseRequest extends MbxBaseModel {

    /**
     * 获取api的uri.<br/>
     * 例如：/mgmt/v1/apiKeys
     *
     * @return
     */
    public abstract String getUri();

    /**
     *
     */
    private Map<String, String> headerMap = Collections.emptyMap();

    /**
     * 表单数据
     */
    private Map<String, String> formData = Collections.emptyMap();

    /**
     * Specify the header info of http request
     *
     * @return
     */
    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    /**
     * Specify the header info of http request
     *
     * @return
     */
    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    @MbxIgnored
    public Map<String, String> getFormData() {
        return formData;
    }

    /**
     * 设置表单数据
     *
     * @param formData
     * @return
     */
    public MbxBaseRequest setFormData(Map<String, String> formData) {
        this.formData = formData;
        return this;
    }
}
