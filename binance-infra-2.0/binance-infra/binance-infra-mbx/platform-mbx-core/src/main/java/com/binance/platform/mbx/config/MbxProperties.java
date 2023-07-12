package com.binance.platform.mbx.config;

/**
 * @author Li Fenggang
 * Date: 2020/7/31 3:41 下午
 */
public class MbxProperties {
    private boolean orderForceDelete = false;
    private String defaultMatchingUnitTypeForProduct;

    /** the field "force" for the creating order function */
    public boolean isOrderForceDelete() {
        return orderForceDelete;
    }

    public void setOrderForceDelete(boolean orderForceDelete) {
        this.orderForceDelete = orderForceDelete;
    }

    public String getDefaultMatchingUnitTypeForProduct() {
        return defaultMatchingUnitTypeForProduct;
    }

    public void setDefaultMatchingUnitTypeForProduct(String defaultMatchingUnitTypeForProduct) {
        this.defaultMatchingUnitTypeForProduct = defaultMatchingUnitTypeForProduct;
    }

    @Override
    public String toString() {
        return "MbxProperties{" +
                "orderForceDelete=" + orderForceDelete +
                ", defaultMatchingUnitTypeForProduct='" + defaultMatchingUnitTypeForProduct + '\'' +
                '}';
    }
}
