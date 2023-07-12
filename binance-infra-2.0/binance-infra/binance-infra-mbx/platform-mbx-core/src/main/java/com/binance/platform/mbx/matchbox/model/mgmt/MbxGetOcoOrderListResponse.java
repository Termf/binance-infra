package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:50 上午
 */
public class MbxGetOcoOrderListResponse extends MbxBaseModel {

    private Long orderListId;
    private String contingencyType;
    private String listStatusType;
    private String listOrderStatus;
    private String listClientOrderId;
    private Long transactionTime;
    private String symbol;
    private List<Order> orders;

    public Long getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(Long orderListId) {
        this.orderListId = orderListId;
    }

    public String getContingencyType() {
        return contingencyType;
    }

    public void setContingencyType(String contingencyType) {
        this.contingencyType = contingencyType;
    }

    public String getListStatusType() {
        return listStatusType;
    }

    public void setListStatusType(String listStatusType) {
        this.listStatusType = listStatusType;
    }

    public String getListOrderStatus() {
        return listOrderStatus;
    }

    public void setListOrderStatus(String listOrderStatus) {
        this.listOrderStatus = listOrderStatus;
    }

    public String getListClientOrderId() {
        return listClientOrderId;
    }

    public void setListClientOrderId(String listClientOrderId) {
        this.listClientOrderId = listClientOrderId;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order {
        private String symbol;
        private Long orderId;
        private String clientOrderId;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getClientOrderId() {
            return clientOrderId;
        }

        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }
    }
}
