package com.binance.master.task.handler;

import org.quartz.Trigger;

@FunctionalInterface
public interface TaskHandler {

    public abstract void executor(Trigger trigger, Object params) throws Throwable;


}
