package com.binance.platform.mbx.model.order;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;
import java.util.List;

public class PlaceOcoOrderResponse extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = 4799412715110194749L;

    private Long orderListId;

    private String contingencyType;
    private String listStatusType;
    private String listClientOrderId;
    private Long transactionTime;
    private String symbol;
    private List<Order> orders;
    private List<OrderReport> orderReports;

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

    public List<OrderReport> getOrderReports() {
        return orderReports;
    }

    public void setOrderReports(List<OrderReport> orderReports) {
        this.orderReports = orderReports;
    }

    public static class Order extends ToString {

        /**
         *
         */
        private static final long serialVersionUID = 4323692028007130939L;

        private String clientOrderId;
        private String symbol;
        private Long orderId;

        public String getClientOrderId() {
            return clientOrderId;
        }

        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }

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
    }

    public static class OrderReport extends ToString {

        /**
         *
         */
        private static final long serialVersionUID = 4323692028007130939L;

        private String symbol;
        private BigDecimal cummulativeQuoteQty;
        private String side;
        private String orderListId;
        private BigDecimal executedQty;
        private Long orderId;
        private String clientOrderId;
        private String type;
        private BigDecimal stopPrice;
        private BigDecimal price;
        private Long transactTime;
        private String timeInForce;
        private String status;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getCummulativeQuoteQty() {
            return cummulativeQuoteQty;
        }

        public void setCummulativeQuoteQty(BigDecimal cummulativeQuoteQty) {
            this.cummulativeQuoteQty = cummulativeQuoteQty;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getOrderListId() {
            return orderListId;
        }

        public void setOrderListId(String orderListId) {
            this.orderListId = orderListId;
        }

        public BigDecimal getExecutedQty() {
            return executedQty;
        }

        public void setExecutedQty(BigDecimal executedQty) {
            this.executedQty = executedQty;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getStopPrice() {
            return stopPrice;
        }

        public void setStopPrice(BigDecimal stopPrice) {
            this.stopPrice = stopPrice;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Long getTransactTime() {
            return transactTime;
        }

        public void setTransactTime(Long transactTime) {
            this.transactTime = transactTime;
        }

        public String getTimeInForce() {
            return timeInForce;
        }

        public void setTimeInForce(String timeInForce) {
            this.timeInForce = timeInForce;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
