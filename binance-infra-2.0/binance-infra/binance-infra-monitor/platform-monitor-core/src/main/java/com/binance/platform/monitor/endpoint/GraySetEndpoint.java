package com.binance.platform.monitor.endpoint;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import com.netflix.appinfo.ApplicationInfoManager;

@RestControllerEndpoint(id = "graySetting")
public class GraySetEndpoint {

	@Autowired
	private WebApplicationContext applicationContext;

	@RequestMapping(method = RequestMethod.GET)
	public String invoke(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EurekaRegistration registration = applicationContext.getBean(EurekaRegistration.class);
		String gray = request.getParameter("gray");
		Map<String, String> map = ApplicationInfoManager.getInstance().getInfo().getMetadata();
		map.putAll(registration.getMetadata());
		map.put("envflag", gray);
		System.setProperty("spring.application.envflag",gray);
		ApplicationInfoManager.getInstance().registerAppMetadata(map);
		return "success";
	}
}
