package com.binance.master.task.factory;

import javax.annotation.Resource;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class TaskFactory extends AdaptableJobFactory {

    @Resource
    private AutowireCapableBeanFactory capableBeanFactory;

    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
