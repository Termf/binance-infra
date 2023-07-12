package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOrderListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/19
 */
public class GetOcoOrderListResponseConverter {

    public static GetOcoOrderListResponse convert(MbxGetOcoOrderListResponse mbxResponse) {
        GetOcoOrderListResponse response = new GetOcoOrderListResponse();
        response.setOrderListId(mbxResponse.getOrderListId());
        response.setContingencyType(mbxResponse.getContingencyType());
        response.setListStatusType(mbxResponse.getListStatusType());
        response.setListOrderStatus(mbxResponse.getListOrderStatus());
        response.setListClientOrderId(mbxResponse.getListClientOrderId());
        response.setTransactionTime(mbxResponse.getTransactionTime());
        response.setSymbol(mbxResponse.getSymbol());
        List<MbxGetOcoOrderListResponse.Order> mbxOrders = mbxResponse.getOrders();
        if (mbxOrders != null) {
            List<GetOcoOrderListResponse.Order> orders = new ArrayList<>();
            mbxOrders.forEach(mbxOrder -> {
                GetOcoOrderListResponse.Order order = new GetOcoOrderListResponse.Order();
                order.setSymbol(mbxOrder.getSymbol());
                order.setOrderId(mbxOrder.getOrderId());
                order.setClientOrderId(mbxOrder.getClientOrderId());
                orders.add(order);
            });
            response.setOrders(orders);
        } else {
            response.setOrders(Collections.emptyList());
        }
        return response;

    }
}
