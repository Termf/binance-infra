package com.binance.master.old.models.trade;

import java.math.BigDecimal;
import java.util.Date;

public class OldTradingLedger {
    private Long tranId;

    private Long tradeId;

    private String symbol;

    private String asset;

    private String userId;

    private String direction;

    private BigDecimal commission;

    private String agent1;

    private BigDecimal agentReward1;

    private String agent2;

    private BigDecimal agentReward2;

    private String agent3;

    private BigDecimal agentReward3;

    private String agent4;

    private BigDecimal agentReward4;

    private String agent5;

    private BigDecimal agentReward5;

    private BigDecimal superAgentReward;

    private BigDecimal exchangeIncome;

    private Date time;

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getAgent1() {
        return agent1;
    }

    public void setAgent1(String agent1) {
        this.agent1 = agent1 == null ? null : agent1.trim();
    }

    public BigDecimal getAgentReward1() {
        return agentReward1;
    }

    public void setAgentReward1(BigDecimal agentReward1) {
        this.agentReward1 = agentReward1;
    }

    public String getAgent2() {
        return agent2;
    }

    public void setAgent2(String agent2) {
        this.agent2 = agent2 == null ? null : agent2.trim();
    }

    public BigDecimal getAgentReward2() {
        return agentReward2;
    }

    public void setAgentReward2(BigDecimal agentReward2) {
        this.agentReward2 = agentReward2;
    }

    public String getAgent3() {
        return agent3;
    }

    public void setAgent3(String agent3) {
        this.agent3 = agent3 == null ? null : agent3.trim();
    }

    public BigDecimal getAgentReward3() {
        return agentReward3;
    }

    public void setAgentReward3(BigDecimal agentReward3) {
        this.agentReward3 = agentReward3;
    }

    public String getAgent4() {
        return agent4;
    }

    public void setAgent4(String agent4) {
        this.agent4 = agent4 == null ? null : agent4.trim();
    }

    public BigDecimal getAgentReward4() {
        return agentReward4;
    }

    public void setAgentReward4(BigDecimal agentReward4) {
        this.agentReward4 = agentReward4;
    }

    public String getAgent5() {
        return agent5;
    }

    public void setAgent5(String agent5) {
        this.agent5 = agent5 == null ? null : agent5.trim();
    }

    public BigDecimal getAgentReward5() {
        return agentReward5;
    }

    public void setAgentReward5(BigDecimal agentReward5) {
        this.agentReward5 = agentReward5;
    }

    public BigDecimal getSuperAgentReward() {
        return superAgentReward;
    }

    public void setSuperAgentReward(BigDecimal superAgentReward) {
        this.superAgentReward = superAgentReward;
    }

    public BigDecimal getExchangeIncome() {
        return exchangeIncome;
    }

    public void setExchangeIncome(BigDecimal exchangeIncome) {
        this.exchangeIncome = exchangeIncome;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
