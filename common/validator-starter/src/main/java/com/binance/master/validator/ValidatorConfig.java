package com.binance.master.validator;

import javax.annotation.PostConstruct;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.binance.master.utils.ValidatorUtils;

@Configuration
public class ValidatorConfig {

    @Autowired
    private Validator validator;

    @PostConstruct
    private void init() {
        ValidatorUtils.init(validator);
    }
}
