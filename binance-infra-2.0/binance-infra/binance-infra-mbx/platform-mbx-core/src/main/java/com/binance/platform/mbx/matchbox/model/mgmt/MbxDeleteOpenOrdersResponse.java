package com.binance.platform.mbx.matchbox.model.mgmt;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.List;

/**
 * This is a base class of the response, please refer to its sub classes to get more information.
 *
 * @author Li Fenggang
 * Date: 2020/8/8 4:28 下午
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "contingencyType", visible = true,
        defaultImpl = MbxDeleteOpenOrdersResponse.Order.class)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = MbxDeleteOpenOrdersResponse.OcoOrder.class, name="OCO")
})
public interface MbxDeleteOpenOrdersResponse {

    /**
     * This is a sub class of {@link MbxDeleteOpenOrdersResponse} representing an order.
     */
    public static class Order implements MbxDeleteOpenOrdersResponse {
        /**
         * origClientOrderId : 5wQ9M9RFMZ7DQKMy8huSAm
         * orderId : 303
         * orderListId : -1
         * clientOrderId : 1sSEoXkdRjGNnIhp7OXquu
         * price : 2.00000000
         * origQty : 2.00000000
         * executedQty : 0.00000000
         * cummulativeQuoteQty : 0.00000000
         * status : CANCELED
         * timeInForce : GTC
         * type : LIMIT
         * side : BUY
         */

        private String origClientOrderId;
        private Long orderId;
        private String clientOrderId;
        private String price;
        private String origQty;
        private String executedQty;
        private String cummulativeQuoteQty;
        private String status;
        private String timeInForce;
        private String type;
        private String side;
        private String symbol;
        private Long orderListId;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Long getOrderListId() {
            return orderListId;
        }

        public void setOrderListId(Long orderListId) {
            this.orderListId = orderListId;
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
    }

    /**
     * This is a sub class of {@link MbxDeleteOpenOrdersResponse} representing an OCO order.
     */
    public static class OcoOrder implements MbxDeleteOpenOrdersResponse {

        /**
         * orderListId : 6
         * listStatusType : ALL_DONE
         * listOrderStatus : ALL_DONE
         * listClientOrderId : tZR1O7mIdApoCHBL0QcWoQ
         * transactionTime : 1596873482489
         * orders : [{"symbol":"BTCUSDT","orderId":305,"clientOrderId":"bZcwJyKUH5S5CXEnpoKHSw"},{"symbol":"BTCUSDT","orderId":306,"clientOrderId":"VF5SjCDIOY0wt2Qq6w8IHo"}]
         * orderReports : [{"symbol":"BTCUSDT","origClientOrderId":"bZcwJyKUH5S5CXEnpoKHSw","orderId":305,"orderListId":6,"clientOrderId":"1sSEoXkdRjGNnIhp7OXquu","price":"0.00000000","origQty":"1.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"CANCELED","timeInForce":"GTC","type":"STOP_LOSS","side":"SELL","stopPrice":"8800.00000000"},{"symbol":"BTCUSDT","origClientOrderId":"VF5SjCDIOY0wt2Qq6w8IHo","orderId":306,"orderListId":6,"clientOrderId":"1sSEoXkdRjGNnIhp7OXquu","price":"9100.00000000","origQty":"1.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"CANCELED","timeInForce":"GTC","type":"LIMIT_MAKER","side":"SELL"}]
         */

        private String contingencyType;
        private String listStatusType;
        private String listOrderStatus;
        private String listClientOrderId;
        private Long transactionTime;
        private List<Order> orders;
        private List<OrderReport> orderReports;
        private String symbol;
        private Long orderListId;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

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
             * orderId : 305
             * clientOrderId : bZcwJyKUH5S5CXEnpoKHSw
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
             * origClientOrderId : bZcwJyKUH5S5CXEnpoKHSw
             * orderId : 305
             * orderListId : 6
             * clientOrderId : 1sSEoXkdRjGNnIhp7OXquu
             * price : 0.00000000
             * origQty : 1.00000000
             * executedQty : 0.00000000
             * cummulativeQuoteQty : 0.00000000
             * status : CANCELED
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
}
