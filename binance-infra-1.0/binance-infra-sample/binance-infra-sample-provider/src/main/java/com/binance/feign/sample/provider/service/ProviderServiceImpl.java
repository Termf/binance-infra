package com.binance.feign.sample.provider.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.binance.feign.sample.provider.vo.Pojo;

import io.micrometer.core.annotation.Timed;

@RestController
public class ProviderServiceImpl implements ProviderService {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
            new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ"), true));
    }

    @Override
    @Timed(value = "aws.scrape", longTask = true)
    public String hello(@RequestBody Pojo pojo) {
        return "Hello " + pojo.getName();
    }
}
