package com.binance.platform.swagger.model;

/**
 * @author rony.gu
 * @date 2020/5/13
 */
public class ApiCallStatics {

    private long totalApiCnt;
    private long testPlanApiCnt;

    public long getTotalApiCnt() {
        return totalApiCnt;
    }

    public long getTestPlanApiCnt() {
        return testPlanApiCnt;
    }

    public void setTotalApiCnt(long totalApiCnt) {
        this.totalApiCnt = totalApiCnt;
    }

    public void setTestPlanApiCnt(long testPlanApiCnt) {
        this.testPlanApiCnt = testPlanApiCnt;
    }
}
