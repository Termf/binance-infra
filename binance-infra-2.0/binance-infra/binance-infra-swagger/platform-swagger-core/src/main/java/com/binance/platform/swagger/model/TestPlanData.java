package com.binance.platform.swagger.model;

import java.util.List;

public class TestPlanData {
    private String path;
    private List<ApiCallRecord> testPlanData;

    public String getPath() {
        return path;
    }

    public List<ApiCallRecord> getTestPlanData() {
        return testPlanData;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTestPlanData(List<ApiCallRecord> testPlanData) {
        this.testPlanData = testPlanData;
    }
}
