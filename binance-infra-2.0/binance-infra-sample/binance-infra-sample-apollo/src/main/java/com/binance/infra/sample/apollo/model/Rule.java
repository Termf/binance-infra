package com.binance.infra.sample.apollo.model;

import lombok.Data;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@Data
public class Rule {

    private String id;
    private String name;
    private String expression;
}
