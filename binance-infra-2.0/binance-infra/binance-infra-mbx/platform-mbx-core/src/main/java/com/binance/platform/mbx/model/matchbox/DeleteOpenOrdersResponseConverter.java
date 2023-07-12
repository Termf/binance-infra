package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOpenOrdersResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/9 7:24 上午
 */
public class DeleteOpenOrdersResponseConverter {
    public static DeleteOpenOrdersResponse convert(List<MbxDeleteOpenOrdersResponse> mbxOrders) {
        DeleteOpenOrdersResponse deleteOpenOrdersResponse = new DeleteOpenOrdersResponse();
        if (mbxOrders != null) {
            for (MbxDeleteOpenOrdersResponse mbxOrder : mbxOrders) {
                if (mbxOrder instanceof MbxDeleteOpenOrdersResponse.Order) {
                    // order
                    MbxDeleteOpenOrdersResponse.Order mbxSimpleOrder =
                            (MbxDeleteOpenOrdersResponse.Order) mbxOrder;
                    deleteOpenOrdersResponse.getOrders().add(convert(mbxSimpleOrder));
                } else if (mbxOrder instanceof MbxDeleteOpenOrdersResponse.OcoOrder) {
                    // OCO order
                    MbxDeleteOpenOrdersResponse.OcoOrder mbxOcoOrder =
                            (MbxDeleteOpenOrdersResponse.OcoOrder) mbxOrder;
                    deleteOpenOrdersResponse.getOcoOrders().add(convert(mbxOcoOrder));
                }
            }
        }
        return deleteOpenOrdersResponse;
    }

    private static DeleteOpenOrdersResponse.OcoOrder convert(MbxDeleteOpenOrdersResponse.OcoOrder mbxOcoOrder) {
        DeleteOpenOrdersResponse.OcoOrder ocoOrder = new DeleteOpenOrdersResponse.OcoOrder();
        ocoOrder.setSymbol(mbxOcoOrder.getSymbol());
        ocoOrder.setOrderListId(mbxOcoOrder.getOrderListId());
        ocoOrder.setContingencyType(mbxOcoOrder.getContingencyType());
        ocoOrder.setListStatusType(mbxOcoOrder.getListStatusType());
        ocoOrder.setListOrderStatus(mbxOcoOrder.getListOrderStatus());
        ocoOrder.setListClientOrderId(mbxOcoOrder.getListClientOrderId());
        ocoOrder.setTransactionTime(mbxOcoOrder.getTransactionTime());
        // orders
        List<MbxDeleteOpenOrdersResponse.OcoOrder.Order> mbxOrders = mbxOcoOrder.getOrders();
        if (mbxOrders != null) {
            List<DeleteOpenOrdersResponse.OcoOrder.Order> orders = new ArrayList<>(mbxOrders.size());
            for (MbxDeleteOpenOrdersResponse.OcoOrder.Order mbxOrder : mbxOrders) {
                orders.add(convert(mbxOrder));
            }
            ocoOrder.setOrders(orders);
        } else {
            ocoOrder.setOrders(Collections.emptyList());
        }

        // orderReports
        List<MbxDeleteOpenOrdersResponse.OcoOrder.OrderReport> mbxOrderReports = mbxOcoOrder.getOrderReports();
        if (mbxOrderReports != null) {
            List<DeleteOpenOrdersResponse.OcoOrder.OrderReport> orderReports = new ArrayList<>(mbxOrderReports.size());
            for (MbxDeleteOpenOrdersResponse.OcoOrder.OrderReport mbxReport : mbxOrderReports) {
                orderReports.add(convert(mbxReport));
            }
            ocoOrder.setOrderReports(orderReports);
        } else {
            ocoOrder.setOrderReports(Collections.emptyList());
        }
        return ocoOrder;
    }

    private static DeleteOpenOrdersResponse.OcoOrder.OrderReport convert(MbxDeleteOpenOrdersResponse.OcoOrder.OrderReport mbxReport) {
        DeleteOpenOrdersResponse.OcoOrder.OrderReport orderReport = new DeleteOpenOrdersResponse.OcoOrder.OrderReport();
        orderReport.setSymbol(mbxReport.getSymbol());
        orderReport.setOrigClientOrderId(mbxReport.getOrigClientOrderId());
        orderReport.setOrderId(mbxReport.getOrderId());
        orderReport.setOrderListId(mbxReport.getOrderListId());
        orderReport.setClientOrderId(mbxReport.getClientOrderId());
        orderReport.setPrice(mbxReport.getPrice());
        orderReport.setOrigQty(mbxReport.getOrigQty());
        orderReport.setExecutedQty(mbxReport.getExecutedQty());
        orderReport.setCummulativeQuoteQty(mbxReport.getCummulativeQuoteQty());
        orderReport.setStatus(mbxReport.getStatus());
        orderReport.setTimeInForce(mbxReport.getTimeInForce());
        orderReport.setType(mbxReport.getType());
        orderReport.setSide(mbxReport.getSide());
        orderReport.setStopPrice(mbxReport.getStopPrice());
        return orderReport;
    }

    private static DeleteOpenOrdersResponse.OcoOrder.Order convert(MbxDeleteOpenOrdersResponse.OcoOrder.Order mbxOrder) {
        DeleteOpenOrdersResponse.OcoOrder.Order order = new DeleteOpenOrdersResponse.OcoOrder.Order();
        order.setSymbol(mbxOrder.getSymbol());
        order.setOrderId(mbxOrder.getOrderId());
        order.setClientOrderId(mbxOrder.getClientOrderId());
        return order;
    }

    private static DeleteOpenOrdersResponse.Order convert(
            MbxDeleteOpenOrdersResponse.Order mbxSimpleOrder) {
        DeleteOpenOrdersResponse.Order order = new DeleteOpenOrdersResponse.Order();
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
