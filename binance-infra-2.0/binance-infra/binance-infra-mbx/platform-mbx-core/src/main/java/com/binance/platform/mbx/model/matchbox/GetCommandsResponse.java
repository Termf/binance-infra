package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:34 上午
 */
public class GetCommandsResponse extends ToString {

    /**
     * method : POST
     * url : /mgmt/v1/order
     * authorization : NONE
     * parameters : [{"parameter":"accountId","isMandatory":true},{"parameter":"symbol","isMandatory":true},{"parameter":"side","isMandatory":true},{"parameter":"type","isMandatory":true},{"parameter":"timeInForce","isMandatory":false},{"parameter":"price","isMandatory":false},{"parameter":"quantity","isMandatory":false},{"parameter":"quoteOrderQty","isMandatory":false},{"parameter":"newClientOrderId","isMandatory":false},{"parameter":"stopPrice","isMandatory":false},{"parameter":"icebergQty","isMandatory":false},{"parameter":"newOrderRespType","isMandatory":false},{"parameter":"force","isMandatory":false},{"parameter":"msgAuth","isMandatory":false}]
     */

    private String method;
    private String url;
    private String authorization;
    private List<ParametersBean> parameters;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public List<ParametersBean> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParametersBean> parameters) {
        this.parameters = parameters;
    }

    public static class ParametersBean implements Serializable {
        /**
         * parameter : accountId
         * isMandatory : true
         */

        private String parameter;
        private Boolean isMandatory;

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public Boolean isIsMandatory() {
            return isMandatory;
        }

        public void setIsMandatory(Boolean isMandatory) {
            this.isMandatory = isMandatory;
        }
    }
}
