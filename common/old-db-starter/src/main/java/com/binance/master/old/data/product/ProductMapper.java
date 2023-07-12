package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.product.Product;
import org.apache.ibatis.annotations.Param;

@OldDB
public interface ProductMapper {
    Product fetchProductByProductCode(@Param("productCode") String productCode);
}