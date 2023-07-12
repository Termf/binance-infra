package com.binance.master.old.data.charge;

import org.apache.ibatis.annotations.Param;

import com.binance.master.commons.Page;
import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldUserCharge;
import com.binance.master.old.models.charge.OldUserChargeKey;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@OldDB
public interface OldUserChargeMapper {
    int deleteByPrimaryKey(OldUserChargeKey key);

    int insert(OldUserCharge record);

    int insertSelective(OldUserCharge record);

    OldUserCharge selectByPrimaryKey(OldUserChargeKey key);

    int updateByPrimaryKeySelective(OldUserCharge record);

    int updateByPrimaryKey(OldUserCharge record);

    OldUserCharge getLastUserCharge(@Param("userId") String userId, @Param("coin") String coin,
            @Param("type") String type);

    List<Map<String, Object>> getChargeList(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("status") Integer status, @Param("userId") String userId, @Param("coin") String coin,
            @Param("lang") String lang, @Param("notStatus") Integer notStatus, @Param("type") String type,
            @Param("page") Page page);

    long getChargeListCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("status") Integer status, @Param("userId") String userId, @Param("coin") String coin,
            @Param("notStatus") Integer notStatus, @Param("type") String type);
    
    OldUserCharge queryUnique(@Param("coin") String coin,@Param("txId")String txId,@Param("targetAddress")String targetAddress,@Param("targetAddressTag")String targetAddressTag);
    
    List<OldUserCharge> queryALL(@Param("limit") Long limit, @Param("maxId") Long maxId);
    
    List<OldUserCharge> getWaitBookkeepingList(@Param("status") Integer status, @Param("coin") String coin, @Param("type") String type,@Param("freeUserChargeAmount")BigDecimal freeUserChargeAmount,@Param("startNo")Integer startNo,
            @Param("page") Page page);
    
    int updateConfirmDeposit(OldUserCharge record);
    
    int updateUnlockDeposit(OldUserCharge record);
    
    int updateLostDeposit(OldUserCharge record);
    
    int updateLostSuccessDeposit(OldUserCharge record);
    
    List<OldUserCharge> getRepairLostDeposit();
    
    List<OldUserCharge> getBtsDeposit(@Param("coin") String coin);

    List<OldUserCharge> getRepairLostSuccessDeposit();
    
    List<OldUserCharge> queryRollBackPnkDepositRepair(@Param("startTime")Date startTime,@Param("limit") Long limit, @Param("statusList") List<Integer> statusList);
}
