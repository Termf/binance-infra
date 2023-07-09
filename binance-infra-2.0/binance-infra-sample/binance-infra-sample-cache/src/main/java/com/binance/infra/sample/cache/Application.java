package com.binance.infra.sample.cache;

import com.binance.master.constant.Constant;
import com.binance.master.utils.IPUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-17
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty(Constant.LOCAL_IP, IPUtils.getIp());
        SpringApplication.run(Application.class, args);
    }
}
