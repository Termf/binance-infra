package com.binance.platform.mbx.matchbox;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetAggTradesRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetAvgPriceV3Request;
import com.binance.platform.mbx.matchbox.model.rest.RestGetDepthRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetDepthResult;
import com.binance.platform.mbx.matchbox.model.rest.RestGetKlinesRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrRequestV3;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrResponseV3;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceAllV3Request;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceOneV3Request;
import com.binance.platform.mbx.matchbox.model.rest.RestGetTickerPriceV3Response;
import com.binance.platform.mbx.matchbox.processor.MbxRequestProcessor;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 6:56 下午
 */
public class MatchBoxRestService {
    private final MbxRequestProcessor mbxRequestProcessor;

    @Value("${com.binance.matchbox.rest.url}")
    private String restRootUrl;

    private String getRestRootUrl() {
        return restRootUrl;
    }

    public MatchBoxRestService(MbxRequestProcessor mbxRequestProcessor) {
        this.mbxRequestProcessor = mbxRequestProcessor;
    }

    /**
     * Get depth
     *
     * @param request
     * @return
     */
    public MbxResponse<RestGetDepthResult> getDepth(RestGetDepthRequest request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<RestGetDepthResult>() {});
    }

    /**
     * Get klines
     *
     * @param request
     * @return
     */
    public MbxResponse<List<List<String>>> getKlines(RestGetKlinesRequest request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<List<List<String>>>() {});
    }

    /**
     * 当前平均价格
     *
     * @param request
     * @return
     */
    public MbxResponse<String> getAvgPriceV3ForString(RestGetAvgPriceV3Request request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<String>() {});
    }

    /**
     * 获取一个交易对最新价格.
     *
     * @param request
     * @return
     */
    public MbxResponse<RestGetTickerPriceV3Response> getTickerPriceOne(RestGetTickerPriceOneV3Request request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<RestGetTickerPriceV3Response>() {});
    }

    /**
     * 获取所有交易对最新价格.
     *
     * @param request
     * @return
     */
    public MbxResponse<List<RestGetTickerPriceV3Response>> getTickerPriceAll(RestGetTickerPriceAllV3Request request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<List<RestGetTickerPriceV3Response>>() {});
    }

    /**
     * Deprecated since 2020年一季度末
     *
     * @param request
     * @return
     */
    @Deprecated
    public MbxResponse<RestGetTicker24HrResponse> getTicker24Hr(RestGetTicker24HrRequest request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<RestGetTicker24HrResponse>() {});
    }

    /**
     * getTicker24HrV3
     *
     * @param request
     * @return
     */
    public MbxResponse<RestGetTicker24HrResponseV3> getTicker24HrV3(RestGetTicker24HrRequestV3 request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<RestGetTicker24HrResponseV3>() {});
    }

    /**
     * Deprecated since 2020年一季度末
     *
     * @param request
     * @return
     */
    @Deprecated
    public MbxResponse<String> getTicker24HrForString(RestGetTicker24HrRequest request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<String>() {});
    }

    /**
     * 近期成交(归集) v1<br/>
     * Deprecated since 2020年一季度末
     * @param request
     * @return
     */
    @Deprecated
    public MbxResponse<String> getAggTradesForString(RestGetAggTradesRequest request) {
        return mbxRequestProcessor.process(getRestRootUrl(), HttpMethod.GET, request,
                new TypeReference<String>() {});
    }
}
