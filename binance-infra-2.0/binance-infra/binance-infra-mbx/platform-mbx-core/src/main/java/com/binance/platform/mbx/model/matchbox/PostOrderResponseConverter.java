package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 10:20 下午
 */
public class PostOrderResponseConverter {

    public static PostOrderResponse convert(MbxPostOrderResponse mbxPostOrderResponse) {
        PostOrderResponse postOrderResponse = new PostOrderResponse();
        postOrderResponse.setSymbol(mbxPostOrderResponse.getSymbol());
        postOrderResponse.setOrderId(mbxPostOrderResponse.getOrderId());
        postOrderResponse.setOrderListId(mbxPostOrderResponse.getOrderListId());
        postOrderResponse.setClientOrderId(mbxPostOrderResponse.getClientOrderId());
        postOrderResponse.setTransactTime(mbxPostOrderResponse.getTransactTime());
        postOrderResponse.setPrice(mbxPostOrderResponse.getPrice());
        postOrderResponse.setOrigQty(mbxPostOrderResponse.getOrigQty());
        postOrderResponse.setExecutedQty(mbxPostOrderResponse.getExecutedQty());
        postOrderResponse.setCummulativeQuoteQty(mbxPostOrderResponse.getCummulativeQuoteQty());
        postOrderResponse.setStatus(mbxPostOrderResponse.getStatus());
        postOrderResponse.setTimeInForce(mbxPostOrderResponse.getTimeInForce());
        postOrderResponse.setType(mbxPostOrderResponse.getType());
        postOrderResponse.setSide(mbxPostOrderResponse.getSide());
        List<MbxPostOrderResponse.Fill> mbxFills = mbxPostOrderResponse.getFills();

        if (mbxFills != null) {
            List<PostOrderResponse.Fill> targetFills = new ArrayList<>(mbxFills.size());
            for (MbxPostOrderResponse.Fill mbxFill : mbxFills) {
                targetFills.add(convert(mbxFill));
            }
            postOrderResponse.setFills(targetFills);
        } else {
            postOrderResponse.setFills(Collections.emptyList());
        }
        return postOrderResponse;
    }

    private static PostOrderResponse.Fill convert(MbxPostOrderResponse.Fill mbxFill) {
        PostOrderResponse.Fill fill = new PostOrderResponse.Fill();
        fill.setPrice(mbxFill.getPrice());
        fill.setQty(mbxFill.getQty());
        fill.setCommission(mbxFill.getCommission());
        fill.setCommissionAsset(mbxFill.getCommissionAsset());
        fill.setTradeId(mbxFill.getTradeId());
        return fill;

    }
}
