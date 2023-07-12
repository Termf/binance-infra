package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 10:11 下午
 */
public class PostOrderResponse extends ToString {

    /**
     * symbol : BTCUSDT
     * orderId : 300
     * orderListId : -1
     * clientOrderId : pWI19IPc3pFJe5xq9HsOus
     * transactTime : 1596550634284
     * price : 0.00000000
     * origQty : 2.00000000
     * executedQty : 2.00000000
     * cummulativeQuoteQty : 18000.00000000
     * status : FILLED
     * timeInForce : GTC
     * type : MARKET
     * side : SELL
     * fills : [{"price":"9000.00000000","qty":"2.00000000","commission":"28.80000000","commissionAsset":"USDT","tradeId":156}]
     */

    private String symbol;
    private Long orderId;
    private Long orderListId;
    private String clientOrderId;
    private Long transactTime;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String timeInForce;
    private String type;
    private String side;
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
