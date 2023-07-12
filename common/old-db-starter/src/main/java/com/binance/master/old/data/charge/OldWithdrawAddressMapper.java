package com.binance.master.old.data.charge;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldWithdrawAddress;

@OldDB
public interface OldWithdrawAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OldWithdrawAddress record);

    int insertSelective(OldWithdrawAddress record);

    OldWithdrawAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OldWithdrawAddress record);

    int updateByPrimaryKey(OldWithdrawAddress record);

    List<OldWithdrawAddress> getWithdrawWhitelistAddressList(@Param("userId") String userId,
            @Param("whiteStatus") Boolean whiteStatus, @Param("status") String status, @Param("asset") String asset);

    /**
     * 批量真删除
     * 
     * @param userId
     * @param ids
     * @return
     */
    int batchDeletionUserWithdrawWhitelistAddress(@Param("userId") String userId, @Param("ids") List<Long> ids);

    /**
     * 批量假删除
     * 
     * @param userId
     * @param ids
     * @return
     */
    int removeWhiteListAddress(@Param("userId") String userId, @Param("ids") List<Long> ids);

    OldWithdrawAddress queryWithdrawWhitelistAddress(@Param("userId") String userId, @Param("asset") String asset,
            @Param("address") String address, @Param("addressTag") String addressTag, @Param("status") String status);

    int queryWithdrawWhitelistAddressCount(@Param("userId") String userId, @Param("asset") String asset);

    List<OldWithdrawAddress> queryByUuidAndUserId(@Param("userId") String userId, @Param("uuid") String uuid);

    int updateWhiteStatus(@Param("userId") String userId, @Param("ids") List<Long> ids,
            @Param("whiteStatus") Boolean whiteStatus);

    List<OldWithdrawAddress> getListByUserIdAndIdsAndWhiteStatus(@Param("userId") String userId,
            @Param("ids") List<Long> ids, @Param("whiteStatus") Boolean whiteStatus);

    int updateUuid(@Param("userId") String userId, @Param("ids") List<Long> ids, @Param("uuid") String uuid,
            @Param("emailSendTime") Date emailSendTime);
    
    int updateStatus(@Param("userId") String userId, @Param("ids") List<Long> ids,@Param("status") String status);
    
    int deleteWhiteListAddress(@Param("userId") String userId, @Param("ids") List<Long> ids);
}
