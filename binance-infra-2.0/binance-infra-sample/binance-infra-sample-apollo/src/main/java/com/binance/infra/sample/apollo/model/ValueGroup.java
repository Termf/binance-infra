package com.binance.infra.sample.apollo.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 两点需要注意：
 * 1. Spring expression不会在这里评估
 * 2. apollo的发布不会热更新
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@Data
@Component
@ConfigurationProperties("sample.rule")
public class ValueGroup {

    private String id;
    private String name;
    private String expression;
}
