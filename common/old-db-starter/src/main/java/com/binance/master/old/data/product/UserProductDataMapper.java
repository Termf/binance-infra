package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.product.UserProductData;
import org.apache.ibatis.annotations.Param;

@OldDB
public interface UserProductDataMapper {
    UserProductData fetchUserProductData(@Param("userId") String userId, @Param("symbol") String symbol);
}