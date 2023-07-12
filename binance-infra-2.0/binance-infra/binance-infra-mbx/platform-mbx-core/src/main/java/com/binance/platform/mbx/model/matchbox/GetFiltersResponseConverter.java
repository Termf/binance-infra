package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetFiltersResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:21 上午
 */
public class GetFiltersResponseConverter {
    public static GetFiltersResponse convert(MbxGetFiltersResponse mbxGetFiltersResponse) {
        GetFiltersResponse getFiltersResponse = new GetFiltersResponse();
        List<MbxGetFiltersResponse.Api> mbxApiList = mbxGetFiltersResponse.getApi();
        if (mbxApiList != null) {
            List<GetFiltersResponse.Api> apiList = new ArrayList<>(mbxApiList.size());
            for (MbxGetFiltersResponse.Api mbxApi : mbxApiList) {
                apiList.add(convert(mbxApi));
            }
            getFiltersResponse.setApi(apiList);
        } else {
            getFiltersResponse.setApi(Collections.emptyList());
        }

        // engine
        List<MbxGetFiltersResponse.Engine> mbxEngineList = mbxGetFiltersResponse.getEngine();

        if (mbxEngineList != null) {
            List<GetFiltersResponse.Engine> engineList = new ArrayList<>(mbxEngineList.size());
            for (MbxGetFiltersResponse.Engine mbxEngine : mbxEngineList) {
                engineList.add(convert(mbxEngine));
            }
            getFiltersResponse.setEngine(engineList);
        } else {
            getFiltersResponse.setEngine(Collections.emptyList());
        }
        return getFiltersResponse;
    }

    private static GetFiltersResponse.Engine convert(MbxGetFiltersResponse.Engine mbxEngine) {
        GetFiltersResponse.Engine engine = new GetFiltersResponse.Engine();
        engine.setFilterType(mbxEngine.getFilterType());
        engine.setMaxNumOrders(mbxEngine.getMaxNumOrders());
        engine.setMaxNumAlgoOrders(mbxEngine.getMaxNumAlgoOrders());
        engine.setMaxNumIcebergOrders(mbxEngine.getMaxNumIcebergOrders());
        engine.setMaxPosition(mbxEngine.getMaxPosition());
        engine.setEndTime(mbxEngine.getEndTime());
        engine.setExemptAccounts(mbxEngine.getExemptAccounts());
        return engine;
    }

    private static GetFiltersResponse.Api convert(MbxGetFiltersResponse.Api mbxApi) {
        GetFiltersResponse.Api api = new GetFiltersResponse.Api();
        api.setFilterType(mbxApi.getFilterType());
        api.setMinPrice(mbxApi.getMinPrice());
        api.setMaxPrice(mbxApi.getMaxPrice());
        api.setTickSize(mbxApi.getTickSize());
        api.setMultiplierUp(mbxApi.getMultiplierUp());
        api.setMultiplierDown(mbxApi.getMultiplierDown());
        api.setAvgPriceMins(mbxApi.getAvgPriceMins());
        api.setMinQty(mbxApi.getMinQty());
        api.setMaxQty(mbxApi.getMaxQty());
        api.setStepSize(mbxApi.getStepSize());
        api.setMinNotional(mbxApi.getMinNotional());
        api.setApplyToMarket(mbxApi.isApplyToMarket());
        api.setLimit(mbxApi.getLimit());
        return api;
    }
}
