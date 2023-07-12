package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModelProperty;

public class FetchProductsRequest extends ToString {
    private static final long serialVersionUID = -1483984554206476101L;
    @ApiModelProperty
    private String symbol;
    @ApiModelProperty("是否包含etf,true:包含，false不包含")
    private boolean includeEtf;
    @ApiModelProperty
    private Boolean needDesc;
    @ApiModelProperty
    private Boolean isWidget;

    public FetchProductsRequest() {
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public void setIncludeEtf(final boolean includeEtf) {
        this.includeEtf = includeEtf;
    }

    public void setNeedDesc(final Boolean needDesc) {
        this.needDesc = needDesc;
    }

    public void setIsWidget(final Boolean isWidget) {
        this.isWidget = isWidget;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public boolean isIncludeEtf() {
        return this.includeEtf;
    }

    public Boolean getNeedDesc() {
        return this.needDesc;
    }

    public Boolean getIsWidget() {
        return this.isWidget;
    }
}
