package com.binance.platform.monitor.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestControllerEndpoint(id = "requestmapping")
public class RequestMappingEndpoint {

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                urlList.add(url);
            }
        }
        return urlList;
    }
}
