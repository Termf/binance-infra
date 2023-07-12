package com.binance.master.old.models.product;

import java.math.BigDecimal;

public class Product {
    private Integer id;

    private String productCode;

    private String productName;

    private String initial;

    private Byte productType;

    private String productPic;

    private BigDecimal productFee;

    private Integer minProductWithdraw;

    private Integer withdrawIntegerMultiple;

    private String feeSchemeType;

    private BigDecimal minWithdrawFee;

    private Integer departmentId;

    private String custom1;

    private String custom2;

    private String custom3;

    private String simpleDesc;

    private String productDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial == null ? null : initial.trim();
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic == null ? null : productPic.trim();
    }

    public BigDecimal getProductFee() {
        return productFee;
    }

    public void setProductFee(BigDecimal productFee) {
        this.productFee = productFee;
    }

    public Integer getMinProductWithdraw() {
        return minProductWithdraw;
    }

    public void setMinProductWithdraw(Integer minProductWithdraw) {
        this.minProductWithdraw = minProductWithdraw;
    }

    public Integer getWithdrawIntegerMultiple() {
        return withdrawIntegerMultiple;
    }

    public void setWithdrawIntegerMultiple(Integer withdrawIntegerMultiple) {
        this.withdrawIntegerMultiple = withdrawIntegerMultiple;
    }

    public String getFeeSchemeType() {
        return feeSchemeType;
    }

    public void setFeeSchemeType(String feeSchemeType) {
        this.feeSchemeType = feeSchemeType == null ? null : feeSchemeType.trim();
    }

    public BigDecimal getMinWithdrawFee() {
        return minWithdrawFee;
    }

    public void setMinWithdrawFee(BigDecimal minWithdrawFee) {
        this.minWithdrawFee = minWithdrawFee;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1 == null ? null : custom1.trim();
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2 == null ? null : custom2.trim();
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3 == null ? null : custom3.trim();
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc == null ? null : simpleDesc.trim();
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc == null ? null : productDesc.trim();
    }
}