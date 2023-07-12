package com.binance.platform.mbx.client.impl;

import com.binance.platform.mbx.client.TradeClient;
import com.binance.platform.mbx.cloud.model.ProductItemVO;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetAggTradesRequest;
import com.binance.platform.mbx.model.matchbox.GetAggTradesRequestConverter;
import com.binance.platform.mbx.model.trade.GetAggTradesRequest;
import com.binance.platform.mbx.service.ProductService;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 10:06 上午
 */
public class TradeClientImpl implements TradeClient {
    private final ProductService productService;
    private final MatchBoxRestService matchBoxRestService;

    public TradeClientImpl(ProductService productService, MatchBoxRestService matchBoxRestService) {
        this.productService = productService;
        this.matchBoxRestService = matchBoxRestService;
    }

    @Override
    public MbxResponse<String> getAggTrades(GetAggTradesRequest request) throws Exception {
        // ensure product exist
        List<ProductItemVO> productItemVOList = productService.queryProductItemWithCheck(request.getSymbol());

        RestGetAggTradesRequest mbxRequest = GetAggTradesRequestConverter.convert(request);
        MbxResponse<String> mbxResponse = matchBoxRestService.getAggTradesForString(mbxRequest);
        return mbxResponse;
    }
}
