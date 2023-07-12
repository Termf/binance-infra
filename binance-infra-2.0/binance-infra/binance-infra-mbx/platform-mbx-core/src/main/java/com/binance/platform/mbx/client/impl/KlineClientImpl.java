package com.binance.platform.mbx.client.impl;

import com.binance.platform.mbx.client.KlineClient;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetKlinesRequest;
import com.binance.platform.mbx.model.kline.GetKlineRequest;
import com.binance.platform.mbx.model.kline.GetKlinesResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 1:36 下午
 */
public class KlineClientImpl implements KlineClient {
    private final MatchBoxRestService matchBoxRestService;

    public KlineClientImpl(MatchBoxRestService matchBoxRestService) {
        this.matchBoxRestService = matchBoxRestService;
    }

    @Override
    public MbxResponse<GetKlinesResponse> getKlines(GetKlineRequest request) throws Exception {

        RestGetKlinesRequest mbxRequest = new RestGetKlinesRequest(request.getSymbol(), request.getInterval());
        mbxRequest.setLimit(request.getLimit());
        mbxRequest.setStartTime(request.getStartTime());
        mbxRequest.setEndTime(request.getEndTime());

        MbxResponse<List<List<String>>> mbxResponse = matchBoxRestService.getKlines(mbxRequest);
        MbxResponse<GetKlinesResponse> response = new MbxResponse<GetKlinesResponse>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            List<List<String>> data = mbxResponse.getData();
            GetKlinesResponse targetData = new GetKlinesResponse();
            List<GetKlinesResponse.KLineItem> targetItems = targetData.getItems();
            for (List<String> item : data) {
                GetKlinesResponse.KLineItem targetItem = new GetKlinesResponse.KLineItem();
                targetItem.setOpenTime(Long.parseLong(item.get(0)));
                targetItem.setOpen(new BigDecimal(item.get(1)));
                targetItem.setHigh(new BigDecimal(item.get(2)));
                targetItem.setLow(new BigDecimal(item.get(3)));
                targetItem.setClose(new BigDecimal(item.get(4)));
                targetItem.setVolume(new BigDecimal(item.get(5)));
                targetItem.setCloseTime(Long.parseLong(item.get(6)));
                targetItem.setQuoteAssetVolume(new BigDecimal(item.get(7)));
                targetItem.setNumberOfTrades(Long.parseLong(item.get(8)));
                targetItem.setTakerBuyBaseAssetVolume(new BigDecimal(item.get(9)));
                targetItem.setTakerBuyQuoteAssetVolume(new BigDecimal(item.get(10)));
                targetItem.setIgnore(new BigDecimal(item.get(11)));

                targetItems.add(targetItem);
            }

            response.setData(targetData);
        }

        return response;
    }
}
