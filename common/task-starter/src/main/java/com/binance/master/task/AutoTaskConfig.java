package com.binance.master.task;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoTaskConfig {

    @Resource
    private Scheduler scheduler;

    @PostConstruct
    private void initTask() {
        TaskUtils.setScheduler(scheduler);
    }

}
