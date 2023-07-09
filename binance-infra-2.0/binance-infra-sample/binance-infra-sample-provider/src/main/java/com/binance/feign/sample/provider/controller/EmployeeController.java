package com.binance.feign.sample.provider.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binance.feign.sample.provider.controller.SimpleService.Contributor;
import com.binance.feign.sample.provider.controller.SimpleService.GitHub;
import com.binance.feign.sample.provider.domain.Employee;
import com.binance.feign.sample.provider.message.send.DirectSender;
import com.binance.feign.sample.provider.message.send.FanoutSender;
import com.binance.feign.sample.provider.message.send.TopicSender;
import com.binance.feign.sample.provider.redis.RedisService;
import com.binance.feign.sample.provider.service.EmployeeServiceImpl;
import com.binance.platform.MyJsonUtil;
import com.google.common.collect.Lists;

import feign.Feign;
import feign.gson.GsonDecoder;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private TopicSender topicSender;

    @Autowired
    private DirectSender directSender;

    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private RedisService redisService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private feign.Client client;

    @Autowired
    private MeterRegistry meter;

    @RequestMapping("/test")
    @Timed("testtest")
    public Collection<Employee> test() {
        Collection<Employee> employeeList = this.employeeService.getAllEmployees();
        int i = 0;
        for (Employee employee : employeeList) {
            i++;
            topicSender.sendTopic(MyJsonUtil.toJson(employee));
            redisService.setKey("employee_" + i, MyJsonUtil.toJson(employee));
        }
        GitHub github =
            Feign.builder().decoder(new GsonDecoder()).client(client).target(GitHub.class, "https://api.github.com");
        List<Contributor> contributors = github.contributors("OpenFeign", "feign");
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
        return Lists.newArrayList(employeeList);
    }

    @RequestMapping("/test1")
    public Employee test1() {
        return this.employeeService.findEmployee("1");
    }

}
