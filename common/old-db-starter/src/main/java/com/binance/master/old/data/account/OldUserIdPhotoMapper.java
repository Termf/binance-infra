package com.binance.master.old.data.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.javasimon.aop.Monitored;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.account.OldUserIdPhoto;

@OldDB
public interface OldUserIdPhotoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OldUserIdPhoto record);

    int insertSelective(OldUserIdPhoto record);

    OldUserIdPhoto selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OldUserIdPhoto record);

    int updateByPrimaryKey(OldUserIdPhoto record);

    @Monitored
    List<OldUserIdPhoto> selectByUserIds(@Param("userIds") List<String> userIds);

    @Monitored
    List<OldUserIdPhoto> selectByUserId(@Param("userId") String userId);
}
