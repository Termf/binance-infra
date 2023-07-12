package com.binance.platform.mbx.service.impl;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.consumer.ProductApiConsumer;
import com.binance.platform.mbx.cloud.model.FetchProductsRequest;
import com.binance.platform.mbx.cloud.model.FetchProductsResponse;
import com.binance.platform.mbx.cloud.model.GetProductByRuleIdRequest;
import com.binance.platform.mbx.cloud.model.GetProductItemRequest;
import com.binance.platform.mbx.cloud.model.GetSumCirculationBySymbolRequest;
import com.binance.platform.mbx.cloud.model.ProductItemResponse;
import com.binance.platform.mbx.cloud.model.ProductItemVO;
import com.binance.platform.mbx.cloud.model.UpdateTradingProductAfterSetFeeRequest;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.service.ProductService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 12:50 下午
 */
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductApiConsumer productApi;

    public ProductServiceImpl(ProductApiConsumer productApi) {
        this.productApi = productApi;
    }

    private LoadingCache<String, List<ProductItemVO>> productItemCache = CacheBuilder.newBuilder().maximumSize(10)
            .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<String, List<ProductItemVO>>() {
                @Override
                public List<ProductItemVO> load(String key) throws Exception {
                    return getProductItem(key);
                }
            });

    private List<ProductItemVO> getProductItem(String symbol) {

        try {
            GetProductItemRequest req = new GetProductItemRequest();
            req.setSymbol(symbol);
            ProductItemResponse productResponse = productApi.getProductItem(req);
            return productResponse.getProductItems();
        } catch (Exception e) {
            LOGGER.error("调用asset-service getProductItem失败:{}", e);
            throw new MbxException("调用asset-service getProductItem失败");
        }
    }
    @Override
    public List<ProductItemVO> queryProductItem(String symbol) throws Exception {
        return productItemCache.get(symbol);
    }

    @Override
    public List<ProductItemVO> queryProductItemWithCheck(String symbol) throws Exception {
        List<ProductItemVO> productItemVOList = queryProductItem(symbol);
        if (productItemVOList == null || productItemVOList.isEmpty()) {
            throw new MbxException(GeneralCode.PRODUCT_NOT_EXIST);
        }
        return productItemVOList;
    }

    @Override
    public List<FetchProductsResponse> queryProducts() {
        try {
            FetchProductsRequest fetchProductsRequest = new FetchProductsRequest();
            fetchProductsRequest.setIncludeEtf(true);
            return productApi.fetchProducts(fetchProductsRequest);
        } catch (Exception e) {
            LOGGER.error("调用asset-service fetchProducts失败:{}", e);
            throw new MbxException("调用asset-service fetchProducts失败");
        }
    }

    @Override
    public APIResponse<Void> updateTradingProductAfterSetFee(UpdateTradingProductAfterSetFeeRequest request) throws Exception {
        return productApi.updateTradingProductAfterSetFee(request);
    }

    @Override
    public APIResponse<ProductItemResponse> getProductByRuleId(GetProductByRuleIdRequest request) throws Exception {
        return productApi.getProductByRuleId(request);
    }

    @Override
    public APIResponse<Double> getSumCirculationBySymbol(GetSumCirculationBySymbolRequest request) throws Exception {
        return productApi.getSumCirculationBySymbol(request);
    }

}
