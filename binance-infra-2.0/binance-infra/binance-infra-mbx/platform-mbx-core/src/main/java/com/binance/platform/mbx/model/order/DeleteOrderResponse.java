package com.binance.platform.mbx.model.order;

import com.binance.master.commons.ToString;
import com.binance.master.error.ErrorCode;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class DeleteOrderResponse extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = 1026989391831357243L;

    private final List<DelError> errors = Lists.newArrayList();
    private final List<DelCorrect> corrects = Lists.newArrayList();

    public List<DelError> getErrors() {
        return errors;
    }

    public List<DelCorrect> getCorrects() {
        return corrects;
    }

    public DeleteOrderResponse addError(String orderId, String symbol, ErrorCode errorCode) {
        DelError error = new DelError();
        error.setErrorCode(errorCode);
        error.setOrderId(orderId);
        error.setSymbol(symbol);
        this.errors.add(error);
        return this;
    }

    public static class DelError extends ToString {
        private static final long serialVersionUID = -3707090315003652327L;

        private String orderId;
        private String symbol;
        private ErrorCode errorCode;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }
    }

    public static class DelCorrect extends ToString {
        private static final long serialVersionUID = -3707090315003652327L;

        private String orderId;
        private String symbol;
        private ErrorCode errorCode;
        private String origClientOrderId;
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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public String getOrigClientOrderId() {
            return origClientOrderId;
        }

        public void setOrigClientOrderId(String origClientOrderId) {
            this.origClientOrderId = origClientOrderId;
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
    }
}
