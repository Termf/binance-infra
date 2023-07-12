package com.binance.platform.mongo;

import java.util.concurrent.TimeUnit;

import com.binance.platform.mongo.alarm.AlarmChecker;
import com.binance.platform.mongo.monitor.MongoCommandLogger;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

/**
 * mongo command monitor listener
 */
public class MongoCommandListener implements CommandListener {

    private static ThreadLocal<String> commandStrThreadLocal = new ThreadLocal<>();

    @Override
    public void commandStarted(CommandStartedEvent commandStartedEvent) {
        commandStrThreadLocal.set(CommandUtil.leftCommandStr(commandStartedEvent.getCommand().toString()));
        AlarmChecker.commandLimitAlarm(commandStartedEvent.getCommand());
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent commandSucceededEvent) {
        AlarmChecker.commandResultAlarm(commandSucceededEvent.getElapsedTime(TimeUnit.MILLISECONDS), commandSucceededEvent,
                commandStrThreadLocal.get());
        MongoCommandLogger.doLog(commandSucceededEvent.getElapsedTime(TimeUnit.MILLISECONDS), commandSucceededEvent, commandStrThreadLocal.get());
        commandStrThreadLocal.remove();
    }

    @Override
    public void commandFailed(CommandFailedEvent commandFailedEvent) {
        AlarmChecker.commandResultAlarm(commandFailedEvent.getElapsedTime(TimeUnit.MILLISECONDS), commandFailedEvent, commandStrThreadLocal.get());
        MongoCommandLogger.doLog(commandFailedEvent.getElapsedTime(TimeUnit.MILLISECONDS), commandFailedEvent, commandStrThreadLocal.get());
        commandStrThreadLocal.remove();
    }
}
