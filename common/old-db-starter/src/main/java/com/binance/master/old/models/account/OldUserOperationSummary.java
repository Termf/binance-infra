package com.binance.master.old.models.account;

import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUserOperationSummary extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 6264127013688955905L;

    private Date date;

    private String operation;

    private String clientType;

    private String country;

    private String operateSystem;

    private Long requestCount;

    private Long peopleCount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType == null ? null : clientType.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getOperateSystem() {
        return operateSystem;
    }

    public void setOperateSystem(String operateSystem) {
        this.operateSystem = operateSystem == null ? null : operateSystem.trim();
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Long peopleCount) {
        this.peopleCount = peopleCount;
    }
}
