package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 10:56 下午
 */
public class PostOcoOrderResponseConverter {

    public static PostOcoOrderResponse convert(MbxPostOcoOrderResponse mbxPostOcoOrderResponse) {
        PostOcoOrderResponse postOcoOrderResponse = new PostOcoOrderResponse();
        postOcoOrderResponse.setOrderListId(mbxPostOcoOrderResponse.getOrderListId());
        postOcoOrderResponse.setContingencyType(mbxPostOcoOrderResponse.getContingencyType());
        postOcoOrderResponse.setListStatusType(mbxPostOcoOrderResponse.getListStatusType());
        postOcoOrderResponse.setListOrderStatus(mbxPostOcoOrderResponse.getListOrderStatus());
        postOcoOrderResponse.setListClientOrderId(mbxPostOcoOrderResponse.getListClientOrderId());
        postOcoOrderResponse.setTransactionTime(mbxPostOcoOrderResponse.getTransactionTime());
        postOcoOrderResponse.setSymbol(mbxPostOcoOrderResponse.getSymbol());
        postOcoOrderResponse.setOrders(convert(mbxPostOcoOrderResponse.getOrders()));

        // OrderReport
        List<MbxPostOcoOrderResponse.OrderReport> mbxOrderReports = mbxPostOcoOrderResponse.getOrderReports();
        if (mbxOrderReports != null) {
            List<PostOcoOrderResponse.OrderReport> orderReports = new ArrayList<>(mbxOrderReports.size());
            for (MbxPostOcoOrderResponse.OrderReport mbxOrderReport : mbxOrderReports) {
                orderReports.add(convert(mbxOrderReport));
            }
            postOcoOrderResponse.setOrderReports(orderReports);
        } else {
            postOcoOrderResponse.setOrderReports(Collections.emptyList());
        }
        return postOcoOrderResponse;

    }

    private static PostOcoOrderResponse.OrderReport convert(MbxPostOcoOrderResponse.OrderReport mbxOrderReport) {
        PostOcoOrderResponse.OrderReport orderReport = new PostOcoOrderResponse.OrderReport();
        orderReport.setSymbol(mbxOrderReport.getSymbol());
        orderReport.setOrderId(mbxOrderReport.getOrderId());
        orderReport.setOrderListId(mbxOrderReport.getOrderListId());
        orderReport.setClientOrderId(mbxOrderReport.getClientOrderId());
        orderReport.setTransactTime(mbxOrderReport.getTransactTime());
        orderReport.setPrice(mbxOrderReport.getPrice());
        orderReport.setOrigQty(mbxOrderReport.getOrigQty());
        orderReport.setExecutedQty(mbxOrderReport.getExecutedQty());
        orderReport.setCummulativeQuoteQty(mbxOrderReport.getCummulativeQuoteQty());
        orderReport.setStatus(mbxOrderReport.getStatus());
        orderReport.setTimeInForce(mbxOrderReport.getTimeInForce());
        orderReport.setType(mbxOrderReport.getType());
        orderReport.setSide(mbxOrderReport.getSide());
        orderReport.setStopPrice(mbxOrderReport.getStopPrice());
        orderReport.setIcebergQty(mbxOrderReport.getIcebergQty());
        // fills
        List<MbxPostOcoOrderResponse.OrderReport.Fill> mbxFills = mbxOrderReport.getFills();
        if (mbxFills != null) {
            List<PostOcoOrderResponse.OrderReport.Fill> fills = new ArrayList<>(mbxFills.size());
            for (MbxPostOcoOrderResponse.OrderReport.Fill mbxFill : mbxFills) {
                fills.add(convert(mbxFill));
            }
            orderReport.setFills(fills);
        } else {
            orderReport.setFills(Collections.emptyList());
        }
        return orderReport;
    }

    private static PostOcoOrderResponse.OrderReport.Fill convert(MbxPostOcoOrderResponse.OrderReport.Fill mbxFill) {
        PostOcoOrderResponse.OrderReport.Fill fill = new PostOcoOrderResponse.OrderReport.Fill();
        fill.setPrice(mbxFill.getPrice());
        fill.setQty(mbxFill.getQty());
        fill.setCommission(mbxFill.getCommission());
        fill.setCommissionAsset(mbxFill.getCommissionAsset());
        fill.setTradeId(mbxFill.getTradeId());
        return fill;
    }

    private static List<PostOcoOrderResponse.Order> convert(List<MbxPostOcoOrderResponse.Order> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }
        List<PostOcoOrderResponse.Order> targetList = new ArrayList<>(orders.size());
        for (MbxPostOcoOrderResponse.Order order : orders) {
            targetList.add(convert(order));
        }

        return targetList;
    }

    private static PostOcoOrderResponse.Order convert(MbxPostOcoOrderResponse.Order mbxOrder) {
        PostOcoOrderResponse.Order order = new PostOcoOrderResponse.Order();
        order.setSymbol(mbxOrder.getSymbol());
        order.setOrderId(mbxOrder.getOrderId());
        order.setClientOrderId(mbxOrder.getClientOrderId());
        return order;
    }
}
