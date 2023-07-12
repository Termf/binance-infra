package com.binance.platform.websocket.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.binance.platform.websocket.WebSocketServerConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(WebSocketServerConfiguration.class)
public @interface EnableWebSocketServer {

}
