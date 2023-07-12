package com.binance.master.old.data.asset;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.asset.OldAsset;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

@OldDB
public interface AssetMapper {
    /**
     *
     * @mbggenerated 2018-01-22
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-01-22
     */
    int insert(OldAsset record);

    /**
     *
     * @mbggenerated 2018-01-22
     */
    int insertSelective(OldAsset record);

    /**
     *
     * @mbggenerated 2018-01-22
     */
    OldAsset selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-01-22
     */
    int updateByPrimaryKeySelective(OldAsset record);

    /**
     *
     * @mbggenerated 2018-01-22
     */
    int updateByPrimaryKey(OldAsset record);

    List<OldAsset> selectAssetByMap(Map<String, Object> param);

    List<Map<String, Object>> getAllAsset();

    List<Map<String, Object>> getAssetList();
    
    int updateWithdrawFee(@Param("transactionFee")BigDecimal transactionFee,@Param("minProductWithdraw")BigDecimal minProductWithdraw,@Param("assetCode")String assetCode);
}
