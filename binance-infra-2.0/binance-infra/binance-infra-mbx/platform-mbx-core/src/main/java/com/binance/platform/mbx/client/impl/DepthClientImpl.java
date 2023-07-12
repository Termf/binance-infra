package com.binance.platform.mbx.client.impl;

import com.binance.platform.mbx.client.DepthClient;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.model.rest.RestGetDepthRequest;
import com.binance.platform.mbx.matchbox.model.rest.RestGetDepthResult;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.depth.DepthResponse;
import com.binance.platform.mbx.model.depth.GetDepthRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 6:33 下午
 */
public class DepthClientImpl implements DepthClient {
    private final MatchBoxRestService matchBoxRestService;

    public DepthClientImpl(MatchBoxRestService matchBoxRestService) {
        this.matchBoxRestService = matchBoxRestService;
    }

    @Override
    public MbxResponse<DepthResponse> getDepth(GetDepthRequest request) {
        RestGetDepthRequest mbxRequest = new RestGetDepthRequest(request.getSymbol());
        mbxRequest.setLimit(request.getLimit());

        MbxResponse<RestGetDepthResult> mbxResponse = matchBoxRestService.getDepth(mbxRequest);
        MbxResponse<DepthResponse> response = new MbxResponse<DepthResponse>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            RestGetDepthResult data = mbxResponse.getData();
            DepthResponse targetData = new DepthResponse();
            response.setData(targetData);

            targetData.setLastUpdateId(data.getLastUpdateId());
            List<List<BigDecimal>> asks = data.getAsks();
            if (asks != null) {
                List<DepthResponse.Info> targetAsks = targetData.getAsks();
                for (List<BigDecimal> ask : asks) {
                    DepthResponse.Info targetAsk = new DepthResponse.Info();
                    targetAsk.setPrice(ask.get(0));
                    targetAsk.setQty(ask.get(1));

                    targetAsks.add(targetAsk);
                }
            }
            List<List<BigDecimal>> bids = data.getBids();
            if (bids != null) {
                List<DepthResponse.Info> targetBids = targetData.getBids();
                for (List<BigDecimal> bid : bids) {
                    DepthResponse.Info targetBid = new DepthResponse.Info();
                    targetBid.setPrice(bid.get(0));
                    targetBid.setQty(bid.get(1));

                    targetBids.add(targetBid);
                }
            }
        }
        return response;
    }
}
