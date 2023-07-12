package com.binance.master.old.data.withdraw;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.binance.master.commons.Page;
import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.withdraw.OldAssetWithdraw;

@OldDB
public interface OldAssetWithdrawMapper {
    int deleteByPrimaryKey(String id);

    int insert(OldAssetWithdraw record);

    int insertSelective(OldAssetWithdraw record);

    OldAssetWithdraw selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OldAssetWithdraw record);

    int updateByPrimaryKey(OldAssetWithdraw record);

    List<Map<String, Object>> getWithdrawList(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("status") Integer status,
            @Param("userId") String userId, @Param("asset") String coin, @Param("lang") String lang, @Param("page") Page page);

    long getWithdrawListCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("status") Integer status,
            @Param("userId") String userId, @Param("asset") String coin);

    int getRepeatTXCount(@Param("txId") String txId, @Param("ids") List<String> ids);

    int confirmAutoWithdrawStatus(@Param("autoStatus") String autoStatus, @Param("txId") String txId, @Param("txKey") String txKey,
            @Param("info") String info, @Param("confirmationNo") Integer confirmationNo, @Param("ids") List<String> ids);
    
    
    OldAssetWithdraw selectByIdAndUserId(@Param("id") String id,@Param("userId") String userId);
    
    OldAssetWithdraw selectByUuId(@Param("uuid") String uuid);
    
    int updateStatusCancelWithdraw(@Param("id") String id,
            @Param("info") String info, @Param("userId") String userId, @Param("successTime") Date successTime);
    
    int updateStatusCancelOrRefuseWithdraw(@Param("id") String id,@Param("status") Integer status,
            @Param("info") String info, @Param("userId") String userId, @Param("successTime") Date successTime);
    
    Long selectWithdrawByAddress (@Param("userId") String userId,
            @Param("address") String address, @Param("amount") BigDecimal amount);
    
    int updateStatusByPrevStatus(@Param("id") String id,@Param("status") int status,@Param("info") String info,@Param("prevStatus") int prevStatus,@Param("updateTime") Date updateTime);
    
    int updateResendEmailMsg(@Param("id") String id,@Param("resendTime") Date resendTime,@Param("uuid") String uuid);

    long queryMaxBatchId();

    List<Map<String, Object>> getWithdrawListBatch(Map<String, Object> param);

    void takeWithdrawListBatch(Map<String, Object> param);

    List<Map<String, Object>> queryTobeWithdrawList(Map<String, Object> param);
    
    int resetAssetWithdraw(@Param("id") String id);
    
    List<OldAssetWithdraw> getAutoWithdrawCancelList(@Param("status") Integer status,@Param("limitTime") Date limitTime);
    
    List<OldAssetWithdraw> getNeedSuccessWithdrawList(@Param("asset") String asset,@Param("startNo") int startNo,@Param("type") int type,@Param("status") int status);
    
    int resetAutoAssetWithdraw(@Param("uuid") String uuid,@Param("checkSum") String checkSum,@Param("id") String id);

    int updateWithdrawNoPay(@Param("id") String id);

    int updateBatchId(@Param("id") String id, @Param("userId") String userId, @Param("asset") String asset);

    int modifyTxId(@Param("id") String id,@Param("txId") String txId);
    
    List<OldAssetWithdraw> getToBeCancelAssetWithdraw(@Param("userId") String userId);
    
    int updateToBeRefuseWithdraw(@Param("id") String id,@Param("userId") String userId,@Param("status") Integer status,@Param("info") String info,@Param("successTime") Date successTime);
    
    int updateToBackendRefuseWithdraw(@Param("id") String id,@Param("userId") String userId,@Param("status") Integer status,@Param("info") String info,@Param("successTime") Date successTime);
    
    List<OldAssetWithdraw> getLastWithdraw(@Param("userId") String userId,@Param("limit") Integer limit);

    public List<OldAssetWithdraw> getUnsureList(@Param("startTime") Date startTime,@Param("endTime") Date endTime);
    
    int updateUnsureWithdraw(@Param("id") String id,@Param("userId") String userId,@Param("asset") String asset);
    
    BigDecimal getLastOneDayWithdrawAmount(@Param("userId") String userId);
    
    List<OldAssetWithdraw> getAssetWithdrawByUserId(@Param("userId") String userId);
    
    List<OldAssetWithdraw> selectWithdrawFailForAutoRefuse(@Param("asset") String asset,@Param("info") String info,@Param("transferBtc") BigDecimal transferBtc,@Param("updateTime") Date updateTime);

    int updateAssetWithdrawForAutoRefuse(@Param("status") Integer status,@Param("info") String info,@Param("id") String id,@Param("successTime") Date successTime);
    
    List<OldAssetWithdraw> getCheckWithdrawList(@Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
