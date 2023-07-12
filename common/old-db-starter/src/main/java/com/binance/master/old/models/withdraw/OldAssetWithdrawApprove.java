package com.binance.master.old.models.withdraw;

import java.math.BigDecimal;
import java.util.Date;

public class OldAssetWithdrawApprove {
    /** 主键 */
    private Integer id;

    /** asset_withdraw_id */
    private String withdrawId;

    /** 币种 */
    private String asset;

    /** 交易ID */
    private String tranId;
    
    private String txId;
    
    private String address;
    
    private BigDecimal amount;

    /** 用户ID */
    private String userId;

    /** 审批状态(I:待审批 S:成功,R:拒绝) */
    private String approveStatus;

    /** 申请人 */
    private String proposer;

    /** 审批人 */
    private String approver;
    /** 用户申请时间 */
    private Date applyDate;
    /**  */
    private Date proposeDate;

    /**  */
    private Date approveDate;

    /** 备注 */
    private String remark;

    /**
     * 主键
     * @return id 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * asset_withdraw_id
     * @return withdraw_id asset_withdraw_id
     */
    public String getWithdrawId() {
        return withdrawId;
    }

    /**
     * asset_withdraw_id
     * @param withdrawId asset_withdraw_id
     */
    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId == null ? null : withdrawId.trim();
    }

    /**
     * 币种
     * @return asset 币种
     */
    public String getAsset() {
        return asset;
    }

    /**
     * 币种
     * @param asset 币种
     */
    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    /**
     * 交易ID
     * @return tran_id 交易ID
     */
    public String getTranId() {
        return tranId;
    }

    /**
     * 交易ID
     * @param tranId 交易ID
     */
    public void setTranId(String tranId) {
        this.tranId = tranId == null ? null : tranId.trim();
    }

    /**
     * 用户ID
     * @return user_id 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 用户ID
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }


    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    /**
     * 申请人
     * @return proposer 申请人
     */
    public String getProposer() {
        return proposer;
    }

    /**
     * 申请人
     * @param proposer 申请人
     */
    public void setProposer(String proposer) {
        this.proposer = proposer == null ? null : proposer.trim();
    }

    /**
     * 审批人
     * @return approver 审批人
     */
    public String getApprover() {
        return approver;
    }

    /**
     * 审批人
     * @param approver 审批人
     */
    public void setApprover(String approver) {
        this.approver = approver == null ? null : approver.trim();
    }

    /**
     * 
     * @return propose_date 
     */
    public Date getProposeDate() {
        return proposeDate;
    }

    /**
     * 
     * @param proposeDate 
     */
    public void setProposeDate(Date proposeDate) {
        this.proposeDate = proposeDate;
    }

    /**
     * 
     * @return approve_date 
     */
    public Date getApproveDate() {
        return approveDate;
    }

    /**
     * 
     * @param approveDate 
     */
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    
    
}