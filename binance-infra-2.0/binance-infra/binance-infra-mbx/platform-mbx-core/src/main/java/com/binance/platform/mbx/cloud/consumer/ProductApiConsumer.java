package com.binance.platform.mbx.cloud.consumer;

import com.binance.master.models.APIRequest;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.cloud.BaseConsumer;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.model.FetchProductsRequest;
import com.binance.platform.mbx.cloud.model.FetchProductsResponse;
import com.binance.platform.mbx.cloud.model.GetProductByRuleIdRequest;
import com.binance.platform.mbx.cloud.model.GetProductItemRequest;
import com.binance.platform.mbx.cloud.model.GetSumCirculationBySymbolRequest;
import com.binance.platform.mbx.cloud.model.PriceConvertRequest;
import com.binance.platform.mbx.cloud.model.PriceConvertResponse;
import com.binance.platform.mbx.cloud.model.ProductItemResponse;
import com.binance.platform.mbx.cloud.model.UpdateTradingProductAfterSetFeeRequest;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:18 下午
 */
public class ProductApiConsumer extends BaseConsumer {

    private final CloudApiCaller cloudApiCaller;

    public ProductApiConsumer(CloudApiCaller cloudApiCaller) {
        this.cloudApiCaller = cloudApiCaller;
    }

    /**
     *
     * @param request
     * @return
     */
    public ProductItemResponse getProductItem(GetProductItemRequest request) {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<ProductItemResponse> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/mgmt/getProductItem", apiRequest,
                new TypeReference<APIResponse<ProductItemResponse>>() {});
        ProductItemResponse result = ApiResponseUtil.getAPIRequestResponse(response);
        return result;
    }


    public List<FetchProductsResponse> fetchProducts(FetchProductsRequest request) {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<List<FetchProductsResponse>> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/public/product", apiRequest,
                new TypeReference<APIResponse<List<FetchProductsResponse>>>() {});
        List<FetchProductsResponse> result = ApiResponseUtil.getAPIRequestResponse(response);
        return result;
    }

    public APIResponse<PriceConvertResponse> priceConvert(PriceConvertRequest request) throws Exception {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<PriceConvertResponse> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/public/convert",
                apiRequest, new TypeReference<APIResponse<PriceConvertResponse>>() {});
        return response;
    }

    public APIResponse<Void> updateTradingProductAfterSetFee(UpdateTradingProductAfterSetFeeRequest request) throws Exception {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<Void> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/mgmt/updateTradingProduct",
                apiRequest, new TypeReference<APIResponse<Void>>() {});
        return response;
    }

    public APIResponse<ProductItemResponse> getProductByRuleId(GetProductByRuleIdRequest request) throws Exception {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<ProductItemResponse> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/mgmt/getProductByRuleId",
                apiRequest, new TypeReference<APIResponse<ProductItemResponse>>() {});
        return response;
    }

    public APIResponse<Double> getSumCirculationBySymbol(GetSumCirculationBySymbolRequest request) throws Exception {
        APIRequest apiRequest = buildApiRequest(request);

        APIResponse<Double> response = cloudApiCaller.post(ApiConstants.SERVICE_ID_ASSET_SERVICE, "/mgmt/getSumCirculationBySymbol",
                apiRequest, new TypeReference<APIResponse<Double>>() {});
        return response;
    }
}
