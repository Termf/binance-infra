package com.binance.platform.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.websocket.client.WebSocketClient;

@Configuration
@ConditionalOnProperty(prefix = "websocket.client", name = "url")
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketClientConfiguration {

	static {
		System.setProperty("localhost.default.nic.list", "bond0,eth0,em0,br0,en0,gpd0");
	}

	@Autowired
	private WebSocketProperties webSocketProperties;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public WebSocketClient webSocketClient() {
		WebSocketClient webSocketClient = new WebSocketClient(webSocketProperties);
		return webSocketClient;

	}

}
