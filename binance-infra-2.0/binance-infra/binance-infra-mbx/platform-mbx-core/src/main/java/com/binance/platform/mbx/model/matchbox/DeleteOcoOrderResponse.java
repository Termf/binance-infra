package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * DeleteOcoOrderResponse
 *
 * @author Li Fenggang
 * Date: 2020/8/7 5:01 下午
 */
public class DeleteOcoOrderResponse extends ToString {

    /**
     * orderListId : 2
     * contingencyType : OCO
     * listStatusType : ALL_DONE
     * listOrderStatus : ALL_DONE
     * listClientOrderId : w2ExXeo1nM2hzqzlon2uVe
     * transactionTime : 1596790533731
     * symbol : BTCUSDT
     * orders : [{"symbol":"BTCUSDT","orderId":293,"clientOrderId":"a6L4xXPNzH5Akh2lv4QWfW"},{"symbol":"BTCUSDT","orderId":294,"clientOrderId":"OKGmo6AVo4Jxd4nmczhh73"}]
     * orderReports : [{"symbol":"BTCUSDT","origClientOrderId":"a6L4xXPNzH5Akh2lv4QWfW","orderId":293,"orderListId":2,"clientOrderId":"LdOHuVxApGF4n23uOjItNR","price":"0.00000000","origQty":"1.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"EXPIRED","timeInForce":"GTC","type":"STOP_LOSS","side":"SELL","stopPrice":"8800.00000000"},{"symbol":"BTCUSDT","origClientOrderId":"OKGmo6AVo4Jxd4nmczhh73","orderId":294,"orderListId":2,"clientOrderId":"LdOHuVxApGF4n23uOjItNR","price":"9200.00000000","origQty":"1.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"EXPIRED","timeInForce":"GTC","type":"LIMIT_MAKER","side":"SELL"}]
     */

    private Long orderListId;
    private String contingencyType;
    private String listStatusType;
    private String listOrderStatus;
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

    public List<OrderReport> getOrderReports() {
        return orderReports;
    }

    public void setOrderReports(List<OrderReport> orderReports) {
        this.orderReports = orderReports;
    }

    public static class Order implements Serializable {
        /**
         * symbol : BTCUSDT
         * orderId : 293
         * clientOrderId : a6L4xXPNzH5Akh2lv4QWfW
         */

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

    public static class OrderReport implements Serializable {
        /**
         * symbol : BTCUSDT
         * origClientOrderId : a6L4xXPNzH5Akh2lv4QWfW
         * orderId : 293
         * orderListId : 2
         * clientOrderId : LdOHuVxApGF4n23uOjItNR
         * price : 0.00000000
         * origQty : 1.00000000
         * executedQty : 0.00000000
         * cummulativeQuoteQty : 0.00000000
         * status : EXPIRED
         * timeInForce : GTC
         * type : STOP_LOSS
         * side : SELL
         * stopPrice : 8800.00000000
         */

        private String symbol;
        private String origClientOrderId;
        private Long orderId;
        private Long orderListId;
        private String clientOrderId;
        private String price;
        private String origQty;
        private String executedQty;
        private String cummulativeQuoteQty;
        private String status;
        private String timeInForce;
        private String type;
        private String side;
        private String stopPrice;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getOrigClientOrderId() {
            return origClientOrderId;
        }

        public void setOrigClientOrderId(String origClientOrderId) {
            this.origClientOrderId = origClientOrderId;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Long getOrderListId() {
            return orderListId;
        }

        public void setOrderListId(Long orderListId) {
            this.orderListId = orderListId;
        }

        public String getClientOrderId() {
            return clientOrderId;
        }

        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrigQty() {
            return origQty;
        }

        public void setOrigQty(String origQty) {
            this.origQty = origQty;
        }

        public String getExecutedQty() {
            return executedQty;
        }

        public void setExecutedQty(String executedQty) {
            this.executedQty = executedQty;
        }

        public String getCummulativeQuoteQty() {
            return cummulativeQuoteQty;
        }

        public void setCummulativeQuoteQty(String cummulativeQuoteQty) {
            this.cummulativeQuoteQty = cummulativeQuoteQty;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimeInForce() {
            return timeInForce;
        }

        public void setTimeInForce(String timeInForce) {
            this.timeInForce = timeInForce;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getStopPrice() {
            return stopPrice;
        }

        public void setStopPrice(String stopPrice) {
            this.stopPrice = stopPrice;
        }
    }
}