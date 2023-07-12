package com.binance.platform.mbx.client.impl;

import com.binance.master.utils.StringUtils;
import com.binance.platform.mbx.client.PriceClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrRequestV3;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrResponseV3;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceAllV3Request;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceOneV3Request;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceV3Response;
import com.binance.platform.mbx.model.price.Get24hrTickerRequestV3;
import com.binance.platform.mbx.model.price.Get24hrTickerResponseV3;
import com.binance.platform.mbx.model.price.Get24hrTickerResponseV3Converter;
import com.binance.platform.mbx.model.price.GetMbxPriceRequest;
import com.binance.platform.mbx.model.price.GetPriceRequest;
import com.binance.platform.mbx.model.price.GetTickerPriceRequest;
import com.binance.platform.mbx.model.price.SymbolClosePriceResponse;
import com.binance.platform.mbx.model.price.SymbolClosePriceResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 10:05 上午
 */
public class PriceClientImpl implements PriceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceClientImpl.class);
    private final MatchBoxRestService matchBoxRestService;

    public PriceClientImpl(MatchBoxRestService matchBoxRestService) {
        this.matchBoxRestService = matchBoxRestService;
    }

    @Override
    public Double getPrice(GetMbxPriceRequest request) throws MbxException {
        List<SymbolClosePriceResponse> priceList = getSymbolClosePriceResponses(request.getSymbol());
        if (priceList.isEmpty()) {
            return BigDecimal.ZERO.doubleValue();
        } else {
            double price = priceList.get(0).getPrice().doubleValue();
            return price;
        }
    }

    @Override
    public List<SymbolClosePriceResponse> getTickerPrice(GetTickerPriceRequest request) throws MbxException {
        String symbol = request.getSymbol();
        List<SymbolClosePriceResponse> resultList = getSymbolClosePriceResponses(symbol);
        return resultList;
    }

    private List<SymbolClosePriceResponse> getSymbolClosePriceResponses(String symbol) {
        List<SymbolClosePriceResponse> resultList = Collections.emptyList();
        try {

            if (StringUtils.isBlank(symbol)) {
                // all
                RestGetTickerPriceAllV3Request mbxRequest = new RestGetTickerPriceAllV3Request();
                MbxResponse<List<RestGetTickerPriceV3Response>> mbxResponse = matchBoxRestService.getTickerPriceAll(mbxRequest);
                if (mbxResponse.isSuccess()) {
                    List<RestGetTickerPriceV3Response> priceList = mbxResponse.getData();
                    if (priceList != null) {
                        resultList = new ArrayList<>(priceList.size());
                        for (RestGetTickerPriceV3Response mbxPrice : priceList) {
                            SymbolClosePriceResponse symbolClosePriceResponse = SymbolClosePriceResponseConverter.convertFrom(mbxPrice);
                            resultList.add(symbolClosePriceResponse);
                        }
                    }
                }
            } else {
                // one
                RestGetTickerPriceOneV3Request mbxRequest = new RestGetTickerPriceOneV3Request(symbol);
                MbxResponse<RestGetTickerPriceV3Response> mbxResponse = matchBoxRestService.getTickerPriceOne(mbxRequest);
                if (mbxResponse.isSuccess()) {
                    resultList = new ArrayList<>(1);
                    RestGetTickerPriceV3Response price = mbxResponse.getData();
                    resultList.add(SymbolClosePriceResponseConverter.convertFrom(price));
                }
            }
        } catch (MbxException e) {
            LOGGER.warn("suppress MbxException return empty value", e);
        }
        return resultList;
    }

    @Override
    public MbxResponse<String> get24hrTicker(GetPriceRequest request) throws MbxException {

        RestGetTicker24HrRequest mbxRequest = new RestGetTicker24HrRequest(request.getSymbol());
        MbxResponse<String> mbxResponse = matchBoxRestService.getTicker24HrForString(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Get24hrTickerResponseV3> get24hrTickerV3(Get24hrTickerRequestV3 request) throws MbxException {
        RestGetTicker24HrRequestV3 mbxRequest = new RestGetTicker24HrRequestV3(request.getSymbol());
        MbxResponse<RestGetTicker24HrResponseV3> mbxResponse = matchBoxRestService.getTicker24HrV3(mbxRequest);

        MbxResponse<Get24hrTickerResponseV3> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(Get24hrTickerResponseV3Converter.convert(mbxResponse.getData()));
        }
        return response;
    }
}
