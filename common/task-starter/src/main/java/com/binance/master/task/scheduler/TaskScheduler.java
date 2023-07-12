package com.binance.master.task.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.binance.master.task.TaskConstants;
import com.binance.master.task.handler.TaskHandler;

@Slf4j
public class TaskScheduler implements Job {

    private final static Map<String, Boolean> lockMap = new ConcurrentHashMap<String, Boolean>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey taskKey = context.getTrigger().getJobKey();
        String taskId = taskKey.getName();
        if (lockMap.get(taskId) == null) {
            lockMap.put(taskId, true);
        }
        if (lockMap.get(taskId)) {
            try {
                lockMap.put(taskId, false);
                JobDataMap jobDataMap = context.getMergedJobDataMap();
                TaskHandler handler = (TaskHandler) jobDataMap.get(TaskConstants.HANDLER_KEY);
                handler.executor(context.getTrigger(), jobDataMap.get(TaskConstants.HANDLER_PARAMS));
            } catch (Throwable e) {
                log.error("TaskId:{},任务执行异常:", taskId, e);
            } finally {
                lockMap.put(taskId, true);
            }
        }
    }

}
