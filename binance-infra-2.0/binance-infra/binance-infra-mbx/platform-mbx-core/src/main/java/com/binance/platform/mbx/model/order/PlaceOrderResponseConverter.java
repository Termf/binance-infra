package com.binance.platform.mbx.model.order;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceOrderResponseConverter {
    public static PlaceOrderResponse convert(MbxPostOrderResponse mbxPostOrderResponse) {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setSymbol(mbxPostOrderResponse.getSymbol());
        placeOrderResponse.setOrderId(mbxPostOrderResponse.getOrderId());
        placeOrderResponse.setOrderListId(mbxPostOrderResponse.getOrderListId());
        placeOrderResponse.setClientOrderId(mbxPostOrderResponse.getClientOrderId());
        placeOrderResponse.setTransactTime(mbxPostOrderResponse.getTransactTime());
        placeOrderResponse.setPrice(mbxPostOrderResponse.getPrice());
        placeOrderResponse.setOrigQty(mbxPostOrderResponse.getOrigQty());
        placeOrderResponse.setExecutedQty(mbxPostOrderResponse.getExecutedQty());
        placeOrderResponse.setCummulativeQuoteQty(mbxPostOrderResponse.getCummulativeQuoteQty());
        placeOrderResponse.setStatus(mbxPostOrderResponse.getStatus());
        placeOrderResponse.setTimeInForce(mbxPostOrderResponse.getTimeInForce());
        placeOrderResponse.setType(mbxPostOrderResponse.getType());
        placeOrderResponse.setSide(mbxPostOrderResponse.getSide());

        List<MbxPostOrderResponse.Fill> mbxFills = mbxPostOrderResponse.getFills();
        if (mbxFills != null) {
            List<PlaceOrderResponse.Fills> targetFills = new ArrayList<>(mbxFills.size());
            for (MbxPostOrderResponse.Fill mbxFill : mbxFills) {
                targetFills.add(convert(mbxFill));
            }
            placeOrderResponse.setFills(targetFills);
        } else {
            placeOrderResponse.setFills(Collections.emptyList());
        }
        return placeOrderResponse;

    }

    private static PlaceOrderResponse.Fills convert(MbxPostOrderResponse.Fill mbxFill) {
        PlaceOrderResponse.Fills fills = new PlaceOrderResponse.Fills();
        fills.setPrice(mbxFill.getPrice());
        fills.setQty(mbxFill.getQty());
        fills.setCommission(mbxFill.getCommission());
        fills.setCommissionAsset(mbxFill.getCommissionAsset());
        fills.setTradeId(mbxFill.getTradeId());
        return fills;
    }
}
