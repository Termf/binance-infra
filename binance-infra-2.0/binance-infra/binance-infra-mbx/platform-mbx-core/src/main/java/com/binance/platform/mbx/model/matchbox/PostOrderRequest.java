package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostOrderRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    // quantity 和 quoteOrderQty 必传一个
    private String quantity;
    @NotEmpty
    private String side;
    @NotEmpty
    private String symbol;
    @NotEmpty
    private String type;
    private String force;
    private String icebergQty;
    private String newClientOrderId;
    private String newOrderRespType;
    private String price;
    private String stopPrice;
    private String targetOrderId;
    private String timeInForce;
    private String msgAuth;
    private Double quoteOrderQty;

    /**
     *
     * @param accountId
     * @param side Valid values are BUY or SELL
     * @param symbol
     * @param type ex:
     * <pre>
     *     TAKE_PROFIT,
     *     MARKET,
     *     LIMIT,
     *     TAKE_PROFIT_LIMIT,
     *     LIMIT_MAKER,
     * </pre>
     *     etc.
     */
    public PostOrderRequest(long accountId, @NotEmpty String side, @NotEmpty String symbol, @NotEmpty String type) {
        this.accountId = accountId;
        this.side = side;
        this.symbol = symbol;
        this.type = type;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getIcebergQty() {
        return icebergQty;
    }

    /**
     * Only required for ICEBERG Orders. Has to be less than the quantity set on the order.
     *
     * @param icebergQty
     */
    public void setIcebergQty(String icebergQty) {
        this.icebergQty = icebergQty;
    }

    public String getNewClientOrderId() {
        return newClientOrderId;
    }

    public void setNewClientOrderId(String newClientOrderId) {
        this.newClientOrderId = newClientOrderId;
    }

    public String getNewOrderRespType() {
        return newOrderRespType;
    }

    /**
     * Valid values are ACK, RESP, FULL
     *
     * @param newOrderRespType
     */
    public void setNewOrderRespType(String newOrderRespType) {
        this.newOrderRespType = newOrderRespType;
    }

    public String getPrice() {
        return price;
    }

    /**
     * Required for Limit Type Orders (i.e. Limit Maker, Stop Loss Limit, Take Profit Limit, etc)
     *
     * @param price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    /**
     * Only required for Algo Orders (e.g. STOP_LOSS,TAKE_PROFIT)
     *
     * @param stopPrice
     */
    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getTargetOrderId() {
        return targetOrderId;
    }

    public void setTargetOrderId(String targetOrderId) {
        this.targetOrderId = targetOrderId;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    /**
     * Used for Limit type orders. Possible values: GTC, IOC, FOK
     *
     * @param timeInForce
     */
    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getMsgAuth() {
        return msgAuth;
    }

    /**
     *  values:
     *  NORMAL
     *  LEGACY_FORCE
     *  LIQUIDATION
     *  MANUAL
     *  When msgAuth is not sent, it defaults to NORMAL.
     *  When the msgAuth is set to anything other than NORMAL, the order or cancel will break tradingPhase, accountType, and symbol permission rules.
     *  When the msgAuth is set to LIQUIDATION or MANUAL, those orders can be queried using the msgAuthOrders endpoint.
     *  NORMAL and LEGACY_FORCE orders are not specially tracked.
     *  MANUAL is intended to be used by the Core Tech team if needed in emergency situations. Please do not use msgAuth=MANUAL.
     *  msgAuth is also included now in all executionReports coming from the private streamer.
     *  The msgAuth for an exeuctionReport is determined by the parent order, so if an order is placed with msgAuth=LIQUIDATION, all trade executionReports from that order will have msgAuth=LIQUIDATION.
     */
    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }

    public Double getQuoteOrderQty() {
        return quoteOrderQty;
    }

    /**
     * “报价总额市价单” 允许用户在市价单MARKET中设置总的购买投入金额或卖出预计回收金额 quoteOrderQty。
     * “报价总额市价单”不会突破LOT_SIZE的限制规则; 报单会按给定的quoteOrderQty尽可能接近地被执行。
     * 以BNBBTC交易对为例:
     * On the BUY side, the order will buy as many BNB as quoteOrderQty BTC can.
     * 买单: 给定quoteOrderQty的BTC会被用来市价买入尽可能多的BNB。
     * On the SELL side, the order will sell as much BNB as needed to receive quoteOrderQty BTC.
     * 卖单: 持有BNB会被尽可能多地以市价卖出以获取给定quoteOrderQty的BTC。
     */
    public void setQuoteOrderQty(Double quoteOrderQty) {
        this.quoteOrderQty = quoteOrderQty;
    }
}