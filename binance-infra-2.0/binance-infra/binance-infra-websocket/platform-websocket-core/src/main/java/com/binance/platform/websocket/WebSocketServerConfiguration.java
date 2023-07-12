package com.binance.platform.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SmartApplicationListener;

import com.binance.platform.websocket.server.WebSocketPushService;
import com.binance.platform.websocket.server.WebSocketServer;

@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketServerConfiguration {

	static {
		System.setProperty("localhost.default.nic.list", "bond0,eth0,em0,br0,en0,gpd0");
	}

	@Autowired
	private WebSocketProperties webSocketProperties;

	private WebSocketServer webSocketServer;

	@Bean
	public CommandLineRunner webSocketServerCommandLineRunner() {
		WebSocketServer webScoketServer = new WebSocketServer(webSocketProperties);
		this.webSocketServer = webScoketServer;
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				webScoketServer.start();
			}

		};
	}

	@Bean
	public SmartApplicationListener webSocketServerDestory() {
		return new SmartApplicationListener() {

			@Override
			public void onApplicationEvent(ApplicationEvent contextClosedEvent) {
				if (webSocketServer != null) {
					webSocketServer.stop();
				}

			}

			@Override
			public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
				return ContextClosedEvent.class.isAssignableFrom(eventType);

			}

		};

	}

	@Bean
	public WebSocketPushService notifyClientService() {
		return new WebSocketPushService();
	}

}
