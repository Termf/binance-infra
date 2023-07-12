package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.product.ProductItem;
import com.binance.master.old.models.product.SymbolMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@OldDB
public interface ProductDataMapper {

    ProductItem fetchProductBySymbol(@Param("symbol") String symbol);

    List<ProductItem> fetchProducts(@Param("symbol") String symbol);

    Map<String, Object> getIndex(@Param("indexId") String indexId);

    Map<String, Object> getIndexBySymbol(@Param("symbol") String symbol);

    /*Map<String, Object> getUserTradeFee(Map<String, Object> param);*/

    Long addProduct(ProductItem product);

    List<ProductItem> getProduct(@Param("symbol") String symbol);

    List<String> getAllSymbols();

    List<String> getVisibleSymbols();

    List<Map<String, Object>> getIndexes();

    List<String> getAllIndexs();

    List<String> getAllIndexsByName();

    List<String> getVisibleIndexs();

    List<String> getSymbolsByIndex(@Param("indexId") String indexId);

    List<Map<String, Object>> getSymbolByIndex(@Param("indexId") String indexId);

    List<String> getSymbolsByAsset(@Param("baseAsset") String baseAsset, @Param("quoteAsset") String quoteAsset);

    List<SymbolMeta> getSymbolsMeta(@Param("symbolMeta") SymbolMeta symbolMeta);
}
