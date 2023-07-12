package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/9 12:05 下午
 */
public class DeleteOrderResponseConverter {

    public static DeleteOrderResponse convert(MbxDeleteOrderResponse mbxResponse) {
        DeleteOrderResponse deleteOrderResponse = new DeleteOrderResponse();
        if (mbxResponse instanceof MbxDeleteOrderResponse.Order) {
            MbxDeleteOrderResponse.Order mbxSimpleOrder = (MbxDeleteOrderResponse.Order)mbxResponse;
            deleteOrderResponse.setOrder(convert(mbxSimpleOrder));
        } else if (mbxResponse instanceof MbxDeleteOrderResponse.OcoOrder) {
            MbxDeleteOrderResponse.OcoOrder ocoOrder = (MbxDeleteOrderResponse.OcoOrder)mbxResponse;
            deleteOrderResponse.setOcoOrder(convert(ocoOrder));
        }

        return deleteOrderResponse;
    }

    private static DeleteOrderResponse.OcoOrder convert(MbxDeleteOrderResponse.OcoOrder mbxOcoOrder) {
        DeleteOrderResponse.OcoOrder ocoOrder = new DeleteOrderResponse.OcoOrder();
        ocoOrder.setSymbol(mbxOcoOrder.getSymbol());
        ocoOrder.setOrderListId(mbxOcoOrder.getOrderListId());
        ocoOrder.setContingencyType(mbxOcoOrder.getContingencyType());
        ocoOrder.setListStatusType(mbxOcoOrder.getListStatusType());
        ocoOrder.setListOrderStatus(mbxOcoOrder.getListOrderStatus());
        ocoOrder.setListClientOrderId(mbxOcoOrder.getListClientOrderId());
        ocoOrder.setTransactionTime(mbxOcoOrder.getTransactionTime());
        // orders
        List<MbxDeleteOrderResponse.OcoOrder.Order> mbxOrders = mbxOcoOrder.getOrders();
        List<DeleteOrderResponse.OcoOrder.Order> orders = new ArrayList<>(mbxOrders.size());
        for (MbxDeleteOrderResponse.OcoOrder.Order mbxOrder : mbxOrders) {
            orders.add(convert(mbxOrder));
        }
        ocoOrder.setOrders(orders);
        // ocoOrders
        List<MbxDeleteOrderResponse.OcoOrder.OrderReport> mbxOrderReports = mbxOcoOrder.getOrderReports();
        if (mbxOrderReports != null) {
            List<DeleteOrderResponse.OcoOrder.OrderReport> orderReports = new ArrayList<>(mbxOrderReports.size());
            for (MbxDeleteOrderResponse.OcoOrder.OrderReport mbxOrderReport : mbxOrderReports) {
                orderReports.add(convert(mbxOrderReport));
            }
            ocoOrder.setOrderReports(orderReports);
        } else {
            ocoOrder.setOrderReports(Collections.emptyList());
        }
        return ocoOrder;
    }

    private static DeleteOrderResponse.OcoOrder.OrderReport convert(MbxDeleteOrderResponse.OcoOrder.OrderReport mbxOrderReport) {
        DeleteOrderResponse.OcoOrder.OrderReport orderReport = new DeleteOrderResponse.OcoOrder.OrderReport();
        orderReport.setSymbol(mbxOrderReport.getSymbol());
        orderReport.setOrigClientOrderId(mbxOrderReport.getOrigClientOrderId());
        orderReport.setOrderId(mbxOrderReport.getOrderId());
        orderReport.setOrderListId(mbxOrderReport.getOrderListId());
        orderReport.setClientOrderId(mbxOrderReport.getClientOrderId());
        orderReport.setPrice(mbxOrderReport.getPrice());
        orderReport.setOrigQty(mbxOrderReport.getOrigQty());
        orderReport.setExecutedQty(mbxOrderReport.getExecutedQty());
        orderReport.setCummulativeQuoteQty(mbxOrderReport.getCummulativeQuoteQty());
        orderReport.setStatus(mbxOrderReport.getStatus());
        orderReport.setTimeInForce(mbxOrderReport.getTimeInForce());
        orderReport.setType(mbxOrderReport.getType());
        orderReport.setSide(mbxOrderReport.getSide());
        orderReport.setStopPrice(mbxOrderReport.getStopPrice());
        return orderReport;
    }

    private static DeleteOrderResponse.OcoOrder.Order convert(MbxDeleteOrderResponse.OcoOrder.Order mbxOrder) {
        DeleteOrderResponse.OcoOrder.Order order = new DeleteOrderResponse.OcoOrder.Order();
        order.setSymbol(mbxOrder.getSymbol());
        order.setOrderId(mbxOrder.getOrderId());
        order.setClientOrderId(mbxOrder.getClientOrderId());
        return order;
    }

    private static DeleteOrderResponse.Order convert(MbxDeleteOrderResponse.Order mbxSimpleOrder) {
        DeleteOrderResponse.Order order = new DeleteOrderResponse.Order();
        order.setSymbol(mbxSimpleOrder.getSymbol());
        order.setOrderListId(mbxSimpleOrder.getOrderListId());
        order.setOrigClientOrderId(mbxSimpleOrder.getOrigClientOrderId());
        order.setOrderId(mbxSimpleOrder.getOrderId());
        order.setClientOrderId(mbxSimpleOrder.getClientOrderId());
        order.setPrice(mbxSimpleOrder.getPrice());
        order.setOrigQty(mbxSimpleOrder.getOrigQty());
        order.setExecutedQty(mbxSimpleOrder.getExecutedQty());
        order.setCummulativeQuoteQty(mbxSimpleOrder.getCummulativeQuoteQty());
        order.setStatus(mbxSimpleOrder.getStatus());
        order.setTimeInForce(mbxSimpleOrder.getTimeInForce());
        order.setType(mbxSimpleOrder.getType());
        order.setSide(mbxSimpleOrder.getSide());
        return order;
    }
}
