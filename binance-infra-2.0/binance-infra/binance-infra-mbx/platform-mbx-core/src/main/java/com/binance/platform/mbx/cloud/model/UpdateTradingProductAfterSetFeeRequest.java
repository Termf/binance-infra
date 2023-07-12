package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

public class UpdateTradingProductAfterSetFeeRequest extends ToString {
    private static final long serialVersionUID = 5157180701487795191L;
    @NotNull
    private ProductItemVO productItem;

    public UpdateTradingProductAfterSetFeeRequest() {
    }

    public ProductItemVO getProductItem() {
        return this.productItem;
    }

    public void setProductItem(final ProductItemVO productItem) {
        this.productItem = productItem;
    }
}