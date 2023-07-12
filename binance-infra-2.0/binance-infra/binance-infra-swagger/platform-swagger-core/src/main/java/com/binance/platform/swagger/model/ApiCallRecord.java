package com.binance.platform.swagger.model;

import java.util.Map;

/**
 * @author rony.gu
 * @date 2020/4/22
 */
public class ApiCallRecord {

    private ApiCallRecordIdentity identity;

    private Map<String, String> parameters;

    private Response response;

    private String createTime;

    private int callCount;

    private Boolean testPlan;

    private String testCaseCode;


    public static class Response {
        private int status;
        private String statusText;
        Map<String, String> headers;
        private String body;

        public void setStatus(int status) {
            this.status = status;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public String getStatusText() {
            return statusText;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getBody() {
            return body;
        }
    }



    public ApiCallRecordIdentity getIdentity() {
        return identity;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Response getResponse() {
        return response;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setIdentity(ApiCallRecordIdentity identity) {
        this.identity = identity;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public Boolean isTestPlan() {
        return testPlan;
    }

    public String getTestCaseCode() {
        return testCaseCode;
    }

    public void setTestPlan(Boolean testPlan) {
        this.testPlan = testPlan;
    }

    public void setTestCaseCode(String testCaseCode) {
        this.testCaseCode = testCaseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApiCallRecord that = (ApiCallRecord) o;
        return identity.equals(that.identity);
    }

    @Override
    public int hashCode() {
        return identity.hashCode();
    }



}
