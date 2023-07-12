package com.binance.master.old.data.userAsset;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.report.TotalVerifyDto;
import com.binance.master.old.models.userAsset.UserAssetLog;
import com.binance.master.old.models.userAsset.UserAssetLogKey;
import com.binance.master.old.models.userAsset.UserAssetLogWithdrawKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@OldDB
public interface UserAssetLogMapper {
    int deleteByPrimaryKey(UserAssetLogKey key);

    int insert(UserAssetLog record);

    int insertSelective(UserAssetLog record);

    UserAssetLog selectByPrimaryKey(UserAssetLogKey key);

    int updateByPrimaryKeySelective(UserAssetLog record);

    int updateByPrimaryKey(UserAssetLog record);

    UserAssetLog getAssetLogByParam(Map<String, Object> map);

    List<UserAssetLogWithdrawKey> findRelationWithdrawRecord(@Param("startTime") Date date);

    List<UserAssetLog> getUalByTranId(Long tranId);

    List<UserAssetLog> getUalByTranIdAndTime(@Param("tranId")Long tranId,@Param("startTime")Date startTime);

    List<UserAssetLog> getDeltaByTranId(@Param("tranIdList")List<Long> tranIds, @Param("startTime")Date startTime);

    List<TotalVerifyDto> getVerifyExchangeInOutFlowResult(@Param("assets")List<String> assets, @Param("endTime")Date endTime);

    List<TotalVerifyDto> getVerifyChargeAndWithdrawResult(@Param("assets")List<String> assets, @Param("endTime")Date endTime);

    List<TotalVerifyDto> getVerifyExchangeIncomeSumResult(@Param("assets")List<String> assets, @Param("endTime")Date endTime);
}
