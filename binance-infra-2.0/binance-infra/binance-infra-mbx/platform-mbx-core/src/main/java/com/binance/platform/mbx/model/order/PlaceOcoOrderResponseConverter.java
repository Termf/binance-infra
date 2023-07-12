package com.binance.platform.mbx.model.order;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceOcoOrderResponseConverter {
    public static PlaceOcoOrderResponse convert(MbxPostOcoOrderResponse mbxPostOcoOrderResponse) {
        PlaceOcoOrderResponse placeOcoOrderResponse = new PlaceOcoOrderResponse();
        placeOcoOrderResponse.setOrderListId(mbxPostOcoOrderResponse.getOrderListId());
        placeOcoOrderResponse.setContingencyType(mbxPostOcoOrderResponse.getContingencyType());
        placeOcoOrderResponse.setListStatusType(mbxPostOcoOrderResponse.getListStatusType());
        placeOcoOrderResponse.setListClientOrderId(mbxPostOcoOrderResponse.getListClientOrderId());
        placeOcoOrderResponse.setTransactionTime(mbxPostOcoOrderResponse.getTransactionTime());
        placeOcoOrderResponse.setSymbol(mbxPostOcoOrderResponse.getSymbol());

        // orders
        List<MbxPostOcoOrderResponse.Order> mbxOrders = mbxPostOcoOrderResponse.getOrders();
        if (mbxOrders != null) {
            List<PlaceOcoOrderResponse.Order> orders = new ArrayList<>(mbxOrders.size());
            for (MbxPostOcoOrderResponse.Order mbxOrder : mbxOrders) {
                orders.add(convert(mbxOrder));
            }
            placeOcoOrderResponse.setOrders(orders);
        } else {
            placeOcoOrderResponse.setOrders(Collections.emptyList());
        }

        // OrderReports
        List<MbxPostOcoOrderResponse.OrderReport> mbxOrderReports = mbxPostOcoOrderResponse.getOrderReports();
        if (mbxOrderReports != null) {
            List<PlaceOcoOrderResponse.OrderReport> orderReports = new ArrayList<>(mbxOrderReports.size());
            for (MbxPostOcoOrderResponse.OrderReport mbxOrderReport : mbxOrderReports) {
                orderReports.add(convert(mbxOrderReport));
            }
            placeOcoOrderResponse.setOrderReports(orderReports);
        } else {
            placeOcoOrderResponse.setOrderReports(Collections.emptyList());
        }
        return placeOcoOrderResponse;
    }

    private static PlaceOcoOrderResponse.OrderReport convert(MbxPostOcoOrderResponse.OrderReport mbxOrderReport) {
        PlaceOcoOrderResponse.OrderReport orderReport = new PlaceOcoOrderResponse.OrderReport();
        orderReport.setSymbol(mbxOrderReport.getSymbol());
        orderReport.setCummulativeQuoteQty(mbxOrderReport.getCummulativeQuoteQty());
        orderReport.setSide(mbxOrderReport.getSide());
        orderReport.setOrderListId(String.valueOf(mbxOrderReport.getOrderListId()));
        orderReport.setExecutedQty(mbxOrderReport.getExecutedQty());
        orderReport.setOrderId(mbxOrderReport.getOrderId());
        orderReport.setClientOrderId(mbxOrderReport.getClientOrderId());
        orderReport.setType(mbxOrderReport.getType());
        orderReport.setStopPrice(mbxOrderReport.getStopPrice());
        orderReport.setPrice(mbxOrderReport.getPrice());
        orderReport.setTransactTime(mbxOrderReport.getTransactTime());
        orderReport.setTimeInForce(mbxOrderReport.getTimeInForce());
        orderReport.setStatus(mbxOrderReport.getStatus());
        return orderReport;
    }

    private static PlaceOcoOrderResponse.Order convert(MbxPostOcoOrderResponse.Order mbxOrder) {
        PlaceOcoOrderResponse.Order order = new PlaceOcoOrderResponse.Order();
        order.setClientOrderId(mbxOrder.getClientOrderId());
        order.setSymbol(mbxOrder.getSymbol());
        order.setOrderId(mbxOrder.getOrderId());
        return order;
    }
}
