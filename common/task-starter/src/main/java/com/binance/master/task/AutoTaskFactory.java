package com.binance.master.task;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.binance.master.task.factory.TaskFactory;

@Configuration
public class AutoTaskFactory {

	@Value("${org.quartz.threadPool.threadCount:10}")
	private String quartzThreadCount = "10";
	
    @Resource
    private TaskExecutor taskExecutor;

    @Bean("taskFactory")
    public TaskFactory jobFactory() {
        return new TaskFactory();
    }

    @Bean("scheduler")
    public SchedulerFactoryBean createScheduler(@Qualifier("taskFactory") TaskFactory taskFactory) {
    	Properties quartzProperties = new Properties();
    	quartzProperties.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, quartzThreadCount);
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setJobFactory(taskFactory);
//        scheduler.setQuartzProperties(quartzProperties);
        scheduler.setTaskExecutor(taskExecutor);// TaskScheduler
        return scheduler;
    }

}
