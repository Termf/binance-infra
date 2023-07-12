package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 10:54 下午
 */
public class MbxPostOcoOrderResponse extends MbxBaseModel {

    /**
     * orderListId : 12
     * contingencyType : OCO
     * listStatusType : EXEC_STARTED
     * listOrderStatus : EXECUTING
     * listClientOrderId : 28
     * transactionTime : 1596965999416
     * symbol : BTCUSDT
     * orders : [{"symbol":"BTCUSDT","orderId":331,"clientOrderId":"29"},{"symbol":"BTCUSDT","orderId":332,"clientOrderId":"kzcFuLwZfG7WSdlOjQm1Nb"}]
     * orderReports : [{"symbol":"BTCUSDT","orderId":331,"orderListId":12,"clientOrderId":"29","transactTime":1596965999416,"price":"9900.00000000","origQty":"10.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"NEW","timeInForce":"GTC","type":"STOP_LOSS_LIMIT","side":"BUY","stopPrice":"9800.00000000","icebergQty":"3.00000000","fills":[]},{"symbol":"BTCUSDT","orderId":332,"orderListId":12,"clientOrderId":"kzcFuLwZfG7WSdlOjQm1Nb","transactTime":1596965999416,"price":"8800.00000000","origQty":"10.00000000","executedQty":"0.00000000","cummulativeQuoteQty":"0.00000000","status":"NEW","timeInForce":"GTC","type":"LIMIT_MAKER","side":"BUY","icebergQty":"8.00000000","fills":[]}]
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
         * orderId : 331
         * clientOrderId : 29
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
         * orderId : 331
         * orderListId : 12
         * clientOrderId : 29
         * transactTime : 1596965999416
         * price : 9900.00000000
         * origQty : 10.00000000
         * executedQty : 0.00000000
         * cummulativeQuoteQty : 0.00000000
         * status : NEW
         * timeInForce : GTC
         * type : STOP_LOSS_LIMIT
         * side : BUY
         * stopPrice : 9800.00000000
         * icebergQty : 3.00000000
         * fills : []
         */

        private String symbol;
        private Long orderId;
        private Long orderListId;
        private String clientOrderId;
        private Long transactTime;
        private BigDecimal price;
        private BigDecimal origQty;
        private BigDecimal executedQty;
        private BigDecimal cummulativeQuoteQty;
        private String status;
        private String timeInForce;
        private String type;
        private String side;
        private BigDecimal stopPrice;
        private BigDecimal icebergQty;
        private List<Fill> fills;

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

        public Long getTransactTime() {
            return transactTime;
        }

        public void setTransactTime(Long transactTime) {
            this.transactTime = transactTime;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getOrigQty() {
            return origQty;
        }

        public void setOrigQty(BigDecimal origQty) {
            this.origQty = origQty;
        }

        public BigDecimal getExecutedQty() {
            return executedQty;
        }

        public void setExecutedQty(BigDecimal executedQty) {
            this.executedQty = executedQty;
        }

        public BigDecimal getCummulativeQuoteQty() {
            return cummulativeQuoteQty;
        }

        public void setCummulativeQuoteQty(BigDecimal cummulativeQuoteQty) {
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

        public BigDecimal getStopPrice() {
            return stopPrice;
        }

        public void setStopPrice(BigDecimal stopPrice) {
            this.stopPrice = stopPrice;
        }

        public BigDecimal getIcebergQty() {
            return icebergQty;
        }

        public void setIcebergQty(BigDecimal icebergQty) {
            this.icebergQty = icebergQty;
        }

        public List<Fill> getFills() {
            return fills;
        }

        public void setFills(List<Fill> fills) {
            this.fills = fills;
        }

        public static class Fill implements Serializable {
            /**
             * price : 9000.00000000
             * qty : 2.00000000
             * commission : 28.80000000
             * commissionAsset : USDT
             * tradeId : 156
             */

            private String price;
            private String qty;
            private String commission;
            private String commissionAsset;
            private Long tradeId;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getQty() {
                return qty;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public String getCommissionAsset() {
                return commissionAsset;
            }

            public void setCommissionAsset(String commissionAsset) {
                this.commissionAsset = commissionAsset;
            }

            public Long getTradeId() {
                return tradeId;
            }

            public void setTradeId(Long tradeId) {
                this.tradeId = tradeId;
            }
        }
    }
}
