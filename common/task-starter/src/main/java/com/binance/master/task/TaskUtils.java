package com.binance.master.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.util.Assert;

import com.binance.master.task.handler.TaskHandler;
import com.binance.master.task.scheduler.TaskScheduler;

public final class TaskUtils {

    private static Scheduler scheduler;

    protected static void setScheduler(Scheduler scheduler) {
        TaskUtils.scheduler = scheduler;
    }

    /**
     * 检查指导task 是否存在
     * 
     * @param taskId
     * @return
     * @throws SchedulerException
     */
    public static boolean checkExists(String taskId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(taskId.toString());
        return scheduler.checkExists(triggerKey);
    }

    /**
     * 添加一个任务计划
     * 
     * @param taskId
     * @param cron
     * @param handler
     * @throws SchedulerException
     */
    public static void addTask(String taskId, String cron, TaskHandler handler, Object params)
            throws SchedulerException {
        Assert.isTrue(!checkExists(taskId), "TaskId已经存在！");
        TriggerKey triggerKey = TriggerKey.triggerKey(taskId);
        JobKey taskkey = new JobKey(taskId);
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger =
                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        JobDetail taskDetail = JobBuilder.newJob(TaskScheduler.class).withIdentity(taskkey).build();
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_KEY, handler);
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_PARAMS, params);
        scheduler.scheduleJob(taskDetail, cronTrigger);
    }

    /**
     * 添加一个在指定时间点开始执行的任务计划。计划总的执行次数=repeatCount+1
     * 
     * @param taskId
     * @param runTime 开始执行时间
     * @param intervalMilliseconds 重复间隔毫秒数
     * @param repeatCount 重复次数
     * @param handler
     * @param params
     * @throws SchedulerException
     */
    public static void addSimpleTask(String taskId, Date runTime, long intervalMilliseconds, int repeatCount,
            TaskHandler handler, Object params) throws SchedulerException {
        Assert.isTrue(!checkExists(taskId), "JobId不存在！");
        TriggerKey triggerKey = TriggerKey.triggerKey(taskId);
        JobKey taskkey = new JobKey(taskId);

        SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
                .withIntervalInMilliseconds(intervalMilliseconds);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(schedBuilder)
                .startAt(runTime).build();
        JobDetail taskDetail = JobBuilder.newJob(TaskScheduler.class).withIdentity(taskkey).build();
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_KEY, handler);
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_PARAMS, params);
        scheduler.scheduleJob(taskDetail, trigger);
    }

    /**
     * 重新安排task执行计划
     * 
     * @param taskId
     * @param cron
     * @param data
     * @throws SchedulerException
     */
    public static void rescheduleJob(String taskId, String cron, TaskHandler handler, Object params)
            throws SchedulerException {
        String taskName = taskId.toString();// 使用taskId作为名称
        Assert.isTrue(checkExists(taskId), "JobId不存在！");
        TriggerKey triggerKey = TriggerKey.triggerKey(taskName);
        JobKey taskkey = new JobKey(taskName);
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger =
                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        JobDetail taskDetail = scheduler.getJobDetail(taskkey);
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_KEY, handler);
        taskDetail.getJobDataMap().put(TaskConstants.HANDLER_PARAMS, params);
        HashSet<Trigger> triggerSet = new HashSet<Trigger>();
        triggerSet.add(cronTrigger);
        scheduler.scheduleJob(taskDetail, triggerSet, true);
    }

    public static List<CronTriggerImpl> getJobList() throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(JobKey.DEFAULT_GROUP));
        List<CronTriggerImpl> triggers = new ArrayList<CronTriggerImpl>();
        for (TriggerKey triggerKey : triggerKeys) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger instanceof CronTriggerImpl) {
                triggers.add((CronTriggerImpl) trigger);
            }
        }
        return triggers;
    }

    /**
     * 删除一个task执行计划
     * 
     * @param taskName
     * @param taskId
     * @throws SchedulerException
     */
    public static boolean removeJob(String taskId) throws SchedulerException {
        Assert.isTrue(checkExists(taskId), "JobId不存在！");
        boolean result = true;
        // TriggerKey triggerKey = TriggerKey.triggerKey(taskId);
        // result = scheduler.unscheduleJob(triggerKey);
        result = scheduler.deleteJob(new JobKey(taskId));
        return result;
    }

    /**
     * 触发一次任务
     * 
     * @param taskId
     * @throws SchedulerException
     */
    public static void runOne(String taskId) throws SchedulerException {
        if (checkExists(taskId)) {
            JobKey jobkey = new JobKey(taskId);
            scheduler.triggerJob(jobkey);
        }
    }
}
