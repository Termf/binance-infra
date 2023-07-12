package com.binance.master.old.models.withdraw;

import java.math.BigDecimal;
import java.util.Date;

public class OldAssetWithdraw {
    private String id;

    private String tranId;

    private String userId;

    private String asset;

    private BigDecimal amount;

    private BigDecimal transferBtcAmount;

    private BigDecimal transactionFee;

    private BigDecimal commissionFee;

    private BigDecimal paidFee;

    private String sourceAddress;

    private String address;

    private String addressTag;

    private String txId;

    private String txKey;

    private String blockHash;

    private Long height;

    private Integer confirmationNo;


    private String type;

    private Integer status;

    private String autoStatus;

    private String checkSum;

    private String info;

    private Date applyTime;

    private Date updateTime;

    private Date successTime;

    private Date resendTime;

    private Integer batchId;

    private String uuid;

    private String clientName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId == null ? null : tranId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTransferBtcAmount() {
        return transferBtcAmount;
    }

    public void setTransferBtcAmount(BigDecimal transferBtcAmount) {
        this.transferBtcAmount = transferBtcAmount;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public BigDecimal getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(BigDecimal commissionFee) {
        this.commissionFee = commissionFee;
    }

    public BigDecimal getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(BigDecimal paidFee) {
        this.paidFee = paidFee;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress == null ? null : sourceAddress.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAddressTag() {
        return addressTag;
    }

    public void setAddressTag(String addressTag) {
        this.addressTag = addressTag == null ? null : addressTag.trim();
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId == null ? null : txId.trim();
    }

    public String getTxKey() {
        return txKey;
    }

    public void setTxKey(String txKey) {
        this.txKey = txKey == null ? null : txKey.trim();
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Integer getConfirmationNo() {
        return confirmationNo;
    }

    public void setConfirmationNo(Integer confirmationNo) {
        this.confirmationNo = confirmationNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(String autoStatus) {
        this.autoStatus = autoStatus == null ? null : autoStatus.trim();
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum == null ? null : checkSum.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Date getResendTime() {
        return resendTime;
    }

    public void setResendTime(Date resendTime) {
        this.resendTime = resendTime;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }
}