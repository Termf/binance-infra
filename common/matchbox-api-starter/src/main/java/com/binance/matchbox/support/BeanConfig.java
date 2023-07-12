package com.binance.matchbox.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Created by Fei.Huang on 2018/5/30.
 */
public class BeanConfig {
    @Bean
    @Primary
    MatchBoxErrorDecoder matchBoxErrorDecoder() {
        return new MatchBoxErrorDecoder();
    }
}
