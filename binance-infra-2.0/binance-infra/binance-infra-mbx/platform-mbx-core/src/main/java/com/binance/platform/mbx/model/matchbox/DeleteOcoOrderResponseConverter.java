package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOcoOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/7 5:07 下午
 */
public class DeleteOcoOrderResponseConverter {
    public static DeleteOcoOrderResponse convert(MbxDeleteOcoOrderResponse mbxDeleteOcoOrderResponse) {
        DeleteOcoOrderResponse deleteOcoOrderResponse = new DeleteOcoOrderResponse();
        deleteOcoOrderResponse.setOrderListId(mbxDeleteOcoOrderResponse.getOrderListId());
        deleteOcoOrderResponse.setContingencyType(mbxDeleteOcoOrderResponse.getContingencyType());
        deleteOcoOrderResponse.setListStatusType(mbxDeleteOcoOrderResponse.getListStatusType());
        deleteOcoOrderResponse.setListOrderStatus(mbxDeleteOcoOrderResponse.getListOrderStatus());
        deleteOcoOrderResponse.setListClientOrderId(mbxDeleteOcoOrderResponse.getListClientOrderId());
        deleteOcoOrderResponse.setTransactionTime(mbxDeleteOcoOrderResponse.getTransactionTime());
        deleteOcoOrderResponse.setSymbol(mbxDeleteOcoOrderResponse.getSymbol());

        // orders
        List<MbxDeleteOcoOrderResponse.Order> mbxOrders = mbxDeleteOcoOrderResponse.getOrders();
        if (mbxOrders != null) {
            List<DeleteOcoOrderResponse.Order> orders = new ArrayList<>(mbxOrders.size());
            for (MbxDeleteOcoOrderResponse.Order mbxOrder : mbxOrders) {
                orders.add(convert(mbxOrder));
            }
            deleteOcoOrderResponse.setOrders(orders);
        } else {
            deleteOcoOrderResponse.setOrders(Collections.emptyList());
        }

        List<MbxDeleteOcoOrderResponse.OrderReport> mbxOrderReports = mbxDeleteOcoOrderResponse.getOrderReports();
        if (mbxOrderReports != null) {
            List<DeleteOcoOrderResponse.OrderReport> orderReports = new ArrayList<>(mbxOrderReports.size());
            for (MbxDeleteOcoOrderResponse.OrderReport mbxOrderReport : mbxOrderReports) {
                orderReports.add(convert(mbxOrderReport));
            }
            deleteOcoOrderResponse.setOrderReports(orderReports);
        } else {
            deleteOcoOrderResponse.setOrderReports(Collections.emptyList());
        }
        return deleteOcoOrderResponse;
    }

    private static DeleteOcoOrderResponse.OrderReport convert(MbxDeleteOcoOrderResponse.OrderReport mbxOrderReport) {
        DeleteOcoOrderResponse.OrderReport orderReport = new DeleteOcoOrderResponse.OrderReport();
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

    private static DeleteOcoOrderResponse.Order convert(MbxDeleteOcoOrderResponse.Order mbxOrder) {
        DeleteOcoOrderResponse.Order order = new DeleteOcoOrderResponse.Order();
        order.setSymbol(mbxOrder.getSymbol());
        order.setOrderId(mbxOrder.getOrderId());
        order.setClientOrderId(mbxOrder.getClientOrderId());
        return order;
    }
}
