package com.binance.platform.swagger.model;

import java.util.List;
import java.util.Objects;

/**
 * @author rony.gu
 * @date 2020/4/22
 */
public class ApiCallRecordIdentity {

    private String id;

    private String endpointName;

    private String methodName;

    private List<String> methodParameterNames;

    public String getEndpointName() {
        return endpointName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getMethodParameterNames() {
        return methodParameterNames;
    }

    public void setMethodParameterNames(List<String> methodParameterNames) {
        this.methodParameterNames = methodParameterNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApiCallRecordIdentity that = (ApiCallRecordIdentity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
