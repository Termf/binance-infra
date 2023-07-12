package com.binance.platform.monitor.endpoint;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestControllerEndpoint(id = "kickout")
public class KickOutEndpoint {

    @Autowired(required = false)
    private HttpClient httpClient;

    @Value("${eureka.client.serviceUrl.defaultZone:http://internal-tk-dev-eureka1-alb-2095628600.ap-northeast-1.elb.amazonaws.com:8761/eureka}")
    private String eurekaUrl;

    @PostConstruct
    public void init() {
        if (this.eurekaUrl.contains(",")) {
            this.eurekaUrl = StringUtils.split(eurekaUrl, ",")[0];
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String invoke(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String kickOutIP = request.getParameter("ip");
        String kickOutApp = request.getParameter("app");
        HttpPut httpPut =
            new HttpPut(eurekaUrl + "apps/" + kickOutApp + "/" + kickOutIP + "/status?value=OUT_OF_SERVICE");
        return EntityUtils.toString(httpClient.execute(httpPut).getEntity(), "UTF-8");
    }
}
