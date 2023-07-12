package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.product.TradingProduct;

@OldDB
public interface TradingProductMapper {
    int deleteByPrimaryKey(String symbol);

    int insert(TradingProduct record);

    int insertSelective(TradingProduct record);

    TradingProduct selectByPrimaryKey(String symbol);

    int updateByPrimaryKeySelective(TradingProduct record);

    int updateByPrimaryKey(TradingProduct record);

    TradingProduct fetchProductBySymbol(String symbol);
}
