package com.binance.platform.mbx.client.impl;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.platform.mbx.client.ProductClient;
import com.binance.platform.mbx.cloud.consumer.ProductApiConsumer;
import com.binance.platform.mbx.cloud.model.PriceConvertResponse;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.binance.platform.mbx.config.MbxPropertiesHolder;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAssetRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostIcebergPartsFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostSymbolRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutOrderTypesRequest;
import com.binance.platform.mbx.model.matchbox.CreateAssetRequestConverter;
import com.binance.platform.mbx.model.matchbox.IceicebergLimitRequestConverter;
import com.binance.platform.mbx.model.matchbox.NumAlgoOrdersRequestConverter;
import com.binance.platform.mbx.model.matchbox.OrderTypeRequestConverter;
import com.binance.platform.mbx.model.matchbox.PriceConvertRequestConverter;
import com.binance.platform.mbx.model.matchbox.PriceConvertVoConverter;
import com.binance.platform.mbx.model.product.AddProductRequest;
import com.binance.platform.mbx.model.product.CreateAssetRequest;
import com.binance.platform.mbx.model.product.IceicebergLimitRequest;
import com.binance.platform.mbx.model.product.NumAlgoOrdersRequest;
import com.binance.platform.mbx.model.product.OrderTypeRequest;
import com.binance.platform.mbx.model.product.PriceConvertRequest;
import com.binance.platform.mbx.model.product.PriceConvertVo;
import com.binance.platform.mbx.util.FormatUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 10:06 上午
 */
public class ProductClientImpl implements ProductClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClientImpl.class);
    private final MatchBoxManagementService matchBoxManagementService;
    private final ProductApiConsumer productApi;

    private String getDefaultMatchingType() {
        return MbxPropertiesHolder.getMbxProperties().getDefaultMatchingUnitTypeForProduct();
    }

    public ProductClientImpl(MatchBoxManagementService matchBoxManagementService, ProductApiConsumer productApi) {
        this.matchBoxManagementService = matchBoxManagementService;
        this.productApi = productApi;
    }

    @Override
    public PriceConvertVo priceConvert(PriceConvertRequest request) throws MbxException {

        com.binance.platform.mbx.cloud.model.PriceConvertRequest apiRequest = PriceConvertRequestConverter.convertFrom(request);
        try {
            APIResponse<PriceConvertResponse> apiResponse = productApi.priceConvert(apiRequest);
            PriceConvertResponse priceConvertResponse = ApiResponseUtil.getAPIRequestResponse(apiResponse);
            PriceConvertVo priceConvertVo = PriceConvertVoConverter.convertTo(priceConvertResponse);
            return priceConvertVo;
        } catch (MbxException e) {
        } catch (Exception e) {
            LOGGER.error("priceConvert", e);
        }
        PriceConvertVo priceConvertVo = new PriceConvertVo();
        priceConvertVo.setAmount(BigDecimal.valueOf(0));
        priceConvertVo.setPrice(BigDecimal.valueOf(0));
        return priceConvertVo;
    }

    @Override
    public MbxResponse<Void> setNumAlgoOrdersFilter(NumAlgoOrdersRequest request) throws MbxException {
        MbxPostNumAlgoOrdersFilterRequest mbxRequest = NumAlgoOrdersRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postNumAlgoOrdersFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setIceicebergLimit(IceicebergLimitRequest request) throws MbxException {
        MbxPostIcebergPartsFilterRequest mbxRequest = IceicebergLimitRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postIcebergPartsFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setProductType(OrderTypeRequest request) throws MbxException {
        MbxPutOrderTypesRequest mbxRequest = OrderTypeRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putOrderTypes(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> addProduct(AddProductRequest request) throws MbxException {
        if(StringUtils.isEmpty(request.getSymbolType())) {
            if(BooleanUtils.isTrue(request.getAllowMargin())) {
                LOGGER.warn("addProduct error:symbolType is empty, when allowMargin is true!");
                throw new MbxException(GeneralCode.ILLEGAL_PARAM, "addProduct error:symbolType is empty, when allowMargin is true!");
            }
        }

        String symbolType = StringUtils.defaultIfBlank(request.getSymbolType(), "SPOT");
        BigDecimal minQty = request.getMinQty() == null ? new BigDecimal("0.00000001") : request.getMinQty();
        BigDecimal maxQty = request.getMaxQty() == null ? new BigDecimal("92141578") : request.getMaxQty();
        BigDecimal maxPrice = request.getMaxPrice() == null ? new BigDecimal("1000") : request.getMaxPrice();
        String commissionType = StringUtils.defaultIfBlank(request.getCommissionType(), "GAS_OR_RECV_OPTIONAL");
        String matchingUnitType = StringUtils.defaultIfBlank(request.getMatchingUnitType(), getDefaultMatchingType());
        String mathSystemType = StringUtils.defaultIfBlank(request.getMathSystemType(), "FULL_CUT");

        MbxPostSymbolRequest mbxRequest = new MbxPostSymbolRequest(request.getSymbol(), symbolType, request.getBaseAsset(),
                request.getQuoteAsset(), minQty.toPlainString(), maxQty.toPlainString(),
                FormatUtils.getPriceNumericFormatter().format(maxPrice), commissionType,
                request.getBaseCommissionDecimalPlaces(), request.getQuoteCommissionDecimalPlaces()
                );
        mbxRequest.setMatchingUnitType(matchingUnitType);
        mbxRequest.setMathSystemType(mathSystemType);
        mbxRequest.setAllowSpot(request.getAllowSpot());
        mbxRequest.setAllowMargin(request.getAllowMargin());

        MbxResponse<Void> mbxResponse = matchBoxManagementService.postSymbol(mbxRequest);

        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> creatAsset(CreateAssetRequest request) throws MbxException {
        MbxPostAssetRequest mbxRequest = CreateAssetRequestConverter.convert(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postAsset(mbxRequest);
        return mbxResponse;
    }
}
