package com.binance.platform.mbx.service;

import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.model.FetchProductsResponse;
import com.binance.platform.mbx.cloud.model.GetProductByRuleIdRequest;
import com.binance.platform.mbx.cloud.model.GetSumCirculationBySymbolRequest;
import com.binance.platform.mbx.cloud.model.ProductItemResponse;
import com.binance.platform.mbx.cloud.model.ProductItemVO;
import com.binance.platform.mbx.cloud.model.UpdateTradingProductAfterSetFeeRequest;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 12:47 下午
 */
public interface ProductService {

    List<ProductItemVO> queryProductItem(String symbol) throws Exception;

    /**
     * Retrieve product and check if the product exists.<br/>
     * Throw an {@link com.binance.platform.mbx.exception.MbxException} if the product doesn't exist.
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    List<ProductItemVO> queryProductItemWithCheck(String symbol) throws Exception;

    List<FetchProductsResponse> queryProducts();

    APIResponse<Void> updateTradingProductAfterSetFee(UpdateTradingProductAfterSetFeeRequest request) throws Exception;

    APIResponse<ProductItemResponse> getProductByRuleId(GetProductByRuleIdRequest request) throws Exception;

    APIResponse<Double> getSumCirculationBySymbol(GetSumCirculationBySymbolRequest request) throws Exception;
}
