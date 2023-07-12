package com.binance.sapi.sample;

import com.binance.platform.sapi.annotation.SapiScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
@SpringBootApplication
@SapiScan("com.binance.sapi.sample.sapi")
public class SapiClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SapiClientApplication.class, args);
    }
}
