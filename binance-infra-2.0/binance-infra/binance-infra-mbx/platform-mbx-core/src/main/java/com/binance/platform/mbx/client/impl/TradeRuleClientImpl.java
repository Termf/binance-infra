package com.binance.platform.mbx.client.impl;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.client.TradeRuleClient;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.model.GetProductByRuleIdRequest;
import com.binance.platform.mbx.cloud.model.GetSumCirculationBySymbolRequest;
import com.binance.platform.mbx.cloud.model.ProductItemResponse;
import com.binance.platform.mbx.cloud.model.ProductItemVO;
import com.binance.platform.mbx.cloud.model.UpdateTradingProductAfterSetFeeRequest;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePositionFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostLotSizeFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostMinNotionalFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostPositionFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostPriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutExchangeGasRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolCommissionRequest;
import com.binance.platform.mbx.model.traderule.QueryAssetGasRequest;
import com.binance.platform.mbx.model.traderule.QueryFeeRequestConverter;
import com.binance.platform.mbx.model.traderule.QueryUserProductFeeRequest;
import com.binance.platform.mbx.model.traderule.QueryUserProductFeeRequestConverter;
import com.binance.platform.mbx.model.traderule.RefreshTradingRuleRequest;
import com.binance.platform.mbx.model.traderule.SetFeeRequest;
import com.binance.platform.mbx.service.ProductService;
import com.binance.platform.mbx.util.FormatUtils;
import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 10:24 上午
 */
public class TradeRuleClientImpl implements TradeRuleClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeRuleClientImpl.class);

    private static final BigDecimal BASIS_POINT = new BigDecimal("10000");
    private static Integer DEFAULT_MAX_QTY = 0;

    private final MatchBoxManagementService matchBoxManagementService;
    private final ProductService productService;

    public TradeRuleClientImpl(MatchBoxManagementService matchBoxManagementService, ProductService productService) {
        this.matchBoxManagementService = matchBoxManagementService;
        this.productService = productService;
    }

    @Override
    public MbxResponse<Void> setAssetGas(QueryAssetGasRequest request) throws MbxException {
        MbxPutExchangeGasRequest mbxRequest = new MbxPutExchangeGasRequest(request.getAsset(),
                String.valueOf(request.getRate().multiply(BASIS_POINT).intValue()));
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putExchangeGas(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setUserProductFee(QueryUserProductFeeRequest request) throws Exception {
        // ensure product exist
        List<ProductItemVO> productItemVOList = productService.queryProductItemWithCheck(request.getSymbol());

        MbxPutAccountSymbolsCommissionRequest mbxRequest = QueryUserProductFeeRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccountSymbolsCommission(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setFee(SetFeeRequest request) throws Exception {
        // ensure product exist
        List<ProductItemVO> productItemVOList = productService.queryProductItemWithCheck(request.getSymbol());
        MbxPutSymbolCommissionRequest mbxRequest = QueryFeeRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbolCommission(mbxRequest);

        if (mbxResponse.isSuccess()) {
            UpdateTradingProductAfterSetFeeRequest serviceRequest = new UpdateTradingProductAfterSetFeeRequest();
            ProductItemVO productItem = productItemVOList.get(0);
            productItem.setBuyerCommission(new BigDecimal(request.getBuyFee()));
            productItem.setSellerCommission(new BigDecimal(request.getSellFee()));
            productItem.setTakerCommission(new BigDecimal(request.getSellFee()));
            productItem.setMakerCommission(new BigDecimal(request.getMakeFee()));
            serviceRequest.setProductItem(productItem);
            APIResponse<Void> serviceResponse = productService.updateTradingProductAfterSetFee(serviceRequest);
            if (APIResponse.Status.OK == serviceResponse.getStatus()) {
                return MbxResponse.success(null);
            } else {
                LOGGER.error("Call updateTradingProductAfterSetFee of the service \"" + ApiConstants.SERVICE_ID_ASSET_SERVICE +
                        "\" failed. " + serviceResponse.toString());
                throw new MbxException(GeneralCode.SYS_ERROR);
            }
        }
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> refreshTradingRule(RefreshTradingRuleRequest request) throws Exception {
        GetProductByRuleIdRequest getProductByRuleIdRequest = new GetProductByRuleIdRequest();
        getProductByRuleIdRequest.setRuleId(request.getRuleId());
        ProductItemResponse productItemResponse = ApiResponseUtil.getAPIRequestResponse(
                this.productService.getProductByRuleId(getProductByRuleIdRequest));
        for (ProductItemVO product : productItemResponse.getProductItems()) {
            GetSumCirculationBySymbolRequest getSumCirculationBySymbolRequest = new GetSumCirculationBySymbolRequest();
            getSumCirculationBySymbolRequest.setSymbol(product.getSymbol());
            Double total = ApiResponseUtil.getAPIRequestResponse(
                    this.productService.getSumCirculationBySymbol(getSumCirculationBySymbolRequest));
            // 持倉量上限絕對值不爲空時以上限絕對值為主
            if ((request.getPositionLimitValue() != null) && (request.getPositionLimitValue() != 0)) {
                long holdingLimit = Math.round(request.getPositionLimitValue().doubleValue());
                LOGGER.info(String.format("%s[总流通为: %s, 持仓上限为: %s", product.getSymbol(), total, holdingLimit));
                // List<String> exemptAccounts =
                // positionFilterExemptDao.fetchExemptAccounts(product.getSymbol());
                List<String> exemptAccounts = null;

                // 设置持仓上限
                setPositionLimit(holdingLimit, exemptAccounts, product.getSymbol());
            } else
                // 持倉量上限絕對值爲空時取上限百分比值
                if (request.getPositionLimit() != null) {
                    // TODO 需要确认逻辑
                    if (total != null && total > 0 && request.getPositionLimit() > 0) {
                        long holdingLimit = (long) (total * request.getPositionLimit());
                        LOGGER.info(String.format("%s[总流通为: %s, 持仓上限为: %s", product.getSymbol(), total, holdingLimit));
                        // List<String> exemptAccounts =
                        // positionFilterExemptDao.fetchExemptAccounts(product.getSymbol());
                        List<String> exemptAccounts = null;
                        // 设置持仓上限
                        setPositionLimit(holdingLimit, exemptAccounts, product.getSymbol());
                    } else if (request.getPositionLimit() != null && request.getPositionLimit() == 0) {
                        // 删除持仓上限
                        MbxDeletePositionFilterRequest mbxRequest = new MbxDeletePositionFilterRequest(product.getSymbol());
                        matchBoxManagementService.deletePositionFilter(mbxRequest);
                    }
                }

            if (request.getMinTrade() != null || request.getMaxTrade() != null || request.getMaxTradeValue() != null
                    || request.getStepSize() != null) {
                // set lotsize filter
                double maxTrade = 0.00;
                // 最大交易单位絕對值不爲空以絕對值爲主
                if ((request.getMaxTradeValue() != null) && (request.getMaxTradeValue() > 0.00))
                    maxTrade = request.getMaxTradeValue();
                else
                    maxTrade = request.getMaxTrade();
                LOGGER.info(String.format("Set lotsize filter for symbol: %s with minQty: %s, maxQty: %s",
                        product.getSymbol(), request.getMinTrade(), request.getMaxTrade()));
                MbxPostLotSizeFilterRequest mbxRequest = new MbxPostLotSizeFilterRequest(
                        FormatUtils.getAssetNumericFormatter().format(Math.ceil(maxTrade)),
                        FormatUtils.getAssetNumericFormatter().format(request.getMinTrade()),
                        FormatUtils.getPriceNumericFormatter().format(request.getStepSize()),
                        product.getSymbol()
                );
                matchBoxManagementService.postLotSizeFilter(mbxRequest);
            }
            if (request.getMinNotional() != null) {
                MbxPostMinNotionalFilterRequest mbxRequest = new MbxPostMinNotionalFilterRequest(
                        // TODO 原逻辑，这里是null，这是不被允许的
                        Boolean.TRUE.toString(),
                        FormatUtils.getPriceNumericFormatter().format(request.getMinNotional()),
                        product.getSymbol()
                );
                matchBoxManagementService.postMinNotionalFilter(mbxRequest);
            }
            Double minPrice = request.getMinPrice() == null ? DEFAULT_MAX_QTY : request.getMinPrice();
            Double maxPrice = request.getMaxPrice() == null ? DEFAULT_MAX_QTY : request.getMaxPrice();
            LOGGER.info(String.format("%s[最小价格: %s, 最大价格: %s", product.getSymbol(), minPrice, maxPrice));
            // set price filter for next day
            MbxPostPriceFilterRequest mbxRequest = new MbxPostPriceFilterRequest(
                    FormatUtils.getPriceNumericFormatter().format(maxPrice),
                    FormatUtils.getPriceNumericFormatter().format(minPrice),
                    product.getSymbol(),
                    FormatUtils.getPriceNumericFormatter().format(request.getTickSize())
            );
            matchBoxManagementService.postPriceFilter(mbxRequest);
        }
        return MbxResponse.success(null);
    }

    private void setPositionLimit(long holdingLimit, List<String> exemptAccounts, String symbol) {
        String exemptAcct = CollectionUtils.isNotEmpty(exemptAccounts) ? Joiner.on(",").join(exemptAccounts) : null;
        MbxPostPositionFilterRequest mbxRequest = new MbxPostPositionFilterRequest(String.valueOf(holdingLimit), symbol);
        mbxRequest.setExemptAcct(exemptAcct);
        matchBoxManagementService.postPositionFilter(mbxRequest);
    }
}
