package com.binance.master.old.data.charge;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.charge.OldBlockLog;
import com.binance.master.old.models.charge.OldBlockLogKey;

@OldDB
public interface OldBlockLogMapper {
    int deleteByPrimaryKey(OldBlockLogKey key);

    int insert(OldBlockLog record);

    int insertSelective(OldBlockLog record);

    OldBlockLog selectByPrimaryKey(OldBlockLogKey key);

    int updateByPrimaryKeySelective(OldBlockLog record);

    int updateByPrimaryKey(OldBlockLog record);
    
    OldBlockLog getLastHeightByCoin(@Param("coin")String coin);
    
    List<OldBlockLog> queryByHeightAndCoin(@Param("coin")String coin,@Param("height")Long height);
    
    OldBlockLog queryByCoinAndBlockHash(@Param("coin")String coin,@Param("blockHash")String blockHash);
    
}
