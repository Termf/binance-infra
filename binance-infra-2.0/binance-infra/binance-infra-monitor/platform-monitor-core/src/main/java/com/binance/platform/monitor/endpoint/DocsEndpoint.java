package com.binance.platform.monitor.endpoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ConfigurationProperties(prefix = "endpoints.docs")
@RestControllerEndpoint(id = "docs")
public class DocsEndpoint {

    private String serverPort;

    public DocsEndpoint(Environment environment) {
        this.serverPort = environment.getProperty("server.port", "8080");
    }

    @RequestMapping(method = RequestMethod.GET)
    public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletName = request.getServerName();
        response.sendRedirect("http://" + servletName + ":" + serverPort + "/docs");
    }

}
