package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import com.google.common.collect.Lists;

import java.util.List;

public class ProductItemResponse extends ToString {
    private static final long serialVersionUID = -3357910480712312469L;
    private final List<ProductItemVO> productItems = Lists.newArrayList();

    public ProductItemResponse() {
    }

    public List<ProductItemVO> getProductItems() {
        return this.productItems;
    }
}