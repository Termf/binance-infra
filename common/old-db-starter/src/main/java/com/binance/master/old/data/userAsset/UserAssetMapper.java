package com.binance.master.old.data.userAsset;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.report.TotalVerifyDto;
import com.binance.master.old.models.userAsset.UserAsset;
import com.binance.master.old.models.userAsset.UserAssetKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@OldDB
public interface UserAssetMapper {
    int deleteByPrimaryKey(UserAssetKey key);

    int insert(UserAsset record);

    int insertSelective(UserAsset record);

    UserAsset selectByPrimaryKey(UserAssetKey key);

    int updateByPrimaryKeySelective(UserAsset record);

    int updateByPrimaryKey(UserAsset record);

    int updateUserAsset(UserAsset userAsset);

    int addOrUpdateUserAsset(UserAsset userAsset);

    UserAsset fetchUserAssetByUidAndAsset(Map<String, Object> param);

    int addCommission(UserAsset userAsset);

    List<Map<String, Object>> getCheckWithdrawing(Map<String, Object> param);

    List<Map<String, Object>> getVerifyWithdrawing(Map<String, Object> param);

    List<Map<String, Object>> getAllAsset();

    List<Map<String, Object>> getUserAssetAmount();

    List<Map<String, Object>> getRetainAsset();

    List<Map<String, Object>> getRetainAssetReally();

    Long getPositionCount();

    List<Map<String, Object>> getSystemRetain();

    List<Map<String, Object>> getAssetPositionCount();

    List<Map<String, Object>> getAssetPositionCountMore();

    List<UserAsset> getAssetByUserId(String uid);

    List<UserAsset> getAllAssetByUserId(Map<String, Object> param);

    List<String> getDistinctAsset();

    List<TotalVerifyDto> getVerifyResult(@Param("assets")List<String> assets, @Param("endTime")Date endTime);
}
