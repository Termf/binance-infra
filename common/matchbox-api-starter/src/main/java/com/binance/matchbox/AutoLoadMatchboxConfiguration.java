package com.binance.matchbox;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.binance.matchbox.api"})
public class AutoLoadMatchboxConfiguration {

}
