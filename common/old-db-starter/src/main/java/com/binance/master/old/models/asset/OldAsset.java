/*
 * Copyright (C) 上海比捷网络科技有限公司.
 *
 * 本系统是商用软件,未经授权擅自复制或传播本程序的部分或全部将是非法的.
 *
 * ============================================================
 *
 * FileName: SystemBillServiceImpl.java
 *
 * Created: [2014-12-22 下午4:34:33] by haolingfeng
 *
 * $Id$
 * 
 * $Revision$
 *
 * $Author$
 *
 * $Date$
 *
 * ============================================================
 * 
 * ProjectName: fbd-core
 * 
 * Description:
 * 
 * ==========================================================
 */
package com.binance.master.old.models.asset;

import java.util.Date;

/**
 * 
 * Copyright (C) 上海比捷网络科技有限公司.
 * 
 * Description: 币种
 * 
 * @author haolingfeng
 * @version 1.0
 * 
 */
public class OldAsset {

    private String id;

    private String assetCode;

    private String assetName;

    private String unit;

    private Double transactionFee;

    private Double commissionRate;

    private Double freeAuditWithdrawAmt;

    private Double freeUserChargeAmount;

    private String minProductWithdraw;

    private String withdrawIntegerMultiple;

    private String confirmTimes;

    private Long chargeLockConfirmTimes;

    private Date createTime;

    private int test;

    private String url;

    private String addressUrl;

    private String blockUrl;

    private Boolean enableCharge;

    private Boolean enableWithdraw;

    private String regEx;

    private String regExTag;

    private Double gas;

    private String parentCode;

    private Boolean isLegalMoney;

    private Double reconciliationAmount;

    private String seqNum;

    private String chineseName;

    private String cnLink;

    private String enLink;

    private String logoUrl;

    private Boolean forceStatus;

    private Boolean resetAddressStatus;

    // in asset expand table
    private String chargeDescCn;

    private String chargeDescEn;

    private String assetLabel;

    private Boolean sameAddress;


    private Boolean depositTipStatus;

    private Boolean dynamicFeeStatus;

    private String depositTipEn;

    private String depositTipCn;

    private String assetLabelEn;

    private String supportMarket;

    private String feeReferenceAsset;

    private Double feeRate;

    private Integer feeDigit;

    private Boolean isTrade;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getCnLink() {
        return cnLink;
    }

    public void setCnLink(String cnLink) {
        this.cnLink = cnLink;
    }

    public String getEnLink() {
        return enLink;
    }

    public void setEnLink(String enLink) {
        this.enLink = enLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(Double transactionFee) {
        this.transactionFee = transactionFee;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    public Boolean getLegalMoney() {
        return isLegalMoney;
    }

    public void setLegalMoney(Boolean legalMoney) {
        isLegalMoney = legalMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getFreeAuditWithdrawAmt() {
        return freeAuditWithdrawAmt;
    }

    public void setFreeAuditWithdrawAmt(Double freeAuditWithdrawAmt) {
        this.freeAuditWithdrawAmt = freeAuditWithdrawAmt;
    }

    public String getWithdrawIntegerMultiple() {
        return withdrawIntegerMultiple;
    }

    public void setWithdrawIntegerMultiple(String withdrawIntegerMultiple) {
        this.withdrawIntegerMultiple = withdrawIntegerMultiple;
    }

    public String getConfirmTimes() {
        return confirmTimes;
    }

    public void setConfirmTimes(String confirmTimes) {
        this.confirmTimes = confirmTimes;
    }

    public Long getChargeLockConfirmTimes() {
        return chargeLockConfirmTimes;
    }

    public void setChargeLockConfirmTimes(Long chargeLockConfirmTimes) {
        this.chargeLockConfirmTimes = chargeLockConfirmTimes;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEnableCharge() {
        return enableCharge;
    }

    public void setEnableCharge(Boolean enableCharge) {
        this.enableCharge = enableCharge;
    }

    public Boolean getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(Boolean enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public String getMinProductWithdraw() {
        return minProductWithdraw;
    }

    public void setMinProductWithdraw(String minProductWithdraw) {
        this.minProductWithdraw = minProductWithdraw;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public Double getGas() {
        return gas;
    }

    public void setGas(Double gas) {
        this.gas = gas;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Boolean getIsLegalMoney() {
        return isLegalMoney;
    }

    public void setIsLegalMoney(Boolean legalMoney) {
        isLegalMoney = legalMoney;
    }

    public Double getFreeUserChargeAmount() {
        return freeUserChargeAmount;
    }

    public void setFreeUserChargeAmount(Double freeUserChargeAmount) {
        this.freeUserChargeAmount = freeUserChargeAmount;
    }

    public Double getReconciliationAmount() {
        return reconciliationAmount;
    }

    public void setReconciliationAmount(Double reconciliationAmount) {
        this.reconciliationAmount = reconciliationAmount;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean getForceStatus() {
        return forceStatus;
    }

    public void setForceStatus(Boolean forceStatus) {
        this.forceStatus = forceStatus;
    }

    public Boolean getResetAddressStatus() {
        return resetAddressStatus;
    }

    public void setResetAddressStatus(Boolean resetAddressStatus) {
        this.resetAddressStatus = resetAddressStatus;
    }


    public String getChargeDescCn() {
        return chargeDescCn;
    }

    public void setChargeDescCn(String chargeDescCn) {
        this.chargeDescCn = chargeDescCn;
    }

    public String getChargeDescEn() {
        return chargeDescEn;
    }

    public void setChargeDescEn(String chargeDescEn) {
        this.chargeDescEn = chargeDescEn;
    }

    public String getAssetLabel() {
        return assetLabel;
    }

    public void setAssetLabel(String assetLabel) {
        this.assetLabel = assetLabel;
    }

    /**
     * @return the sameAddress
     */
    public Boolean getSameAddress() {
        return sameAddress;
    }

    /**
     * @param sameAddress the sameAddress to set
     */
    public void setSameAddress(Boolean sameAddress) {
        this.sameAddress = sameAddress;
    }

    public Boolean getDepositTipStatus() {
        return depositTipStatus;
    }

    public void setDepositTipStatus(Boolean depositTipStatus) {
        this.depositTipStatus = depositTipStatus;
    }

    public String getDepositTipEn() {
        return depositTipEn;
    }

    public void setDepositTipEn(String depositTipEn) {
        this.depositTipEn = depositTipEn;
    }

    public String getDepositTipCn() {
        return depositTipCn;
    }

    public void setDepositTipCn(String depositTipCn) {
        this.depositTipCn = depositTipCn;
    }

    /**
     * @return the regExTag
     */
    public String getRegExTag() {
        return regExTag;
    }

    /**
     * @param regExTag the regExTag to set
     */
    public void setRegExTag(String regExTag) {
        this.regExTag = regExTag;
    }

    public String getAssetLabelEn() {
        return assetLabelEn;
    }

    public void setAssetLabelEn(String assetLabelEn) {
        this.assetLabelEn = assetLabelEn;
    }

    public String getSupportMarket() {
        return supportMarket;
    }

    public void setSupportMarket(String supportMarket) {
        this.supportMarket = supportMarket;
    }

    public String getBlockUrl() {
        return blockUrl;
    }

    public void setBlockUrl(String blockUrl) {
        this.blockUrl = blockUrl;
    }

    public Boolean getDynamicFeeStatus() {
        return dynamicFeeStatus;
    }

    public void setDynamicFeeStatus(Boolean dynamicFeeStatus) {
        this.dynamicFeeStatus = dynamicFeeStatus;
    }

    public String getFeeReferenceAsset() {
        return feeReferenceAsset;
    }

    public void setFeeReferenceAsset(String feeReferenceAsset) {
        this.feeReferenceAsset = feeReferenceAsset;
    }

    public Double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Double feeRate) {
        this.feeRate = feeRate;
    }

    public Integer getFeeDigit() {
        return feeDigit;
    }

    public void setFeeDigit(Integer feeDigit) {
        this.feeDigit = feeDigit;
    }

    public Boolean getTrade() {
        return isTrade;
    }

    public void setTrade(Boolean trade) {
        isTrade = trade;
    }
}
