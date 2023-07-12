package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoAllOrderListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/19
 */
public class GetOcoAllOrderListResponseConverter {

    public static GetOcoAllOrderListResponse convert(MbxGetOcoAllOrderListResponse mbxResponse) {
        GetOcoAllOrderListResponse response = new GetOcoAllOrderListResponse();
        response.setOrderListId(mbxResponse.getOrderListId());
        response.setContingencyType(mbxResponse.getContingencyType());
        response.setListStatusType(mbxResponse.getListStatusType());
        response.setListOrderStatus(mbxResponse.getListOrderStatus());
        response.setListClientOrderId(mbxResponse.getListClientOrderId());
        response.setTransactionTime(mbxResponse.getTransactionTime());
        response.setSymbol(mbxResponse.getSymbol());
        List<MbxGetOcoAllOrderListResponse.Order> mbxOrders = mbxResponse.getOrders();
        if (mbxOrders != null) {
            List<GetOcoAllOrderListResponse.Order> orders = new ArrayList<>();
            mbxOrders.forEach(mbxOrder -> {
                GetOcoAllOrderListResponse.Order order = new GetOcoAllOrderListResponse.Order();
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
