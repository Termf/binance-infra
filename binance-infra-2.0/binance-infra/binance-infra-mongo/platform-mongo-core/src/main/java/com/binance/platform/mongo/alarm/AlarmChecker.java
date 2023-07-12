package com.binance.platform.mongo.alarm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import com.binance.platform.common.AlarmUtil;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.mongo.CommandUtil;
import com.mongodb.event.CommandEvent;
import com.mongodb.event.CommandFailedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmChecker {

    private static String SLOW_COMMAND_TEMPLATE = "mongo慢command告警:【 %s 】command耗时:【 %sS 】UUID:【%s】 \n %s";

    private static String EXCEPTION_COMMAND_TEMPLATE = "mongo command异常告警: 【 %s 】\n %s";

    private static String LIMIT_COMMAND_TEMPLATE = "mongo command超出限制告警: 【 %s 】\n %s";

    /**
     * command result alarm
     * 
     * @param elapsedTime
     * @param commandEvent
     * @param commandStr
     */
    public static void commandResultAlarm(long elapsedTime, CommandEvent commandEvent, String commandStr) {
        try {
            int alarmMongoSeconds = Integer.parseInt(EnvUtil.getProperty("mongo.alarm.time", "5"));
            String appName = EnvUtil.getProperty("spring.application.name");
            if (EnvUtil.isUSA() || !EnvUtil.isAlarmService(appName)) {
                return;
            }

            if (alarmMongoSeconds > 0) {
                if (elapsedTime > alarmMongoSeconds * 1000) {
                    // slow alarm
                    AlarmUtil.alarmTeams(String.format(SLOW_COMMAND_TEMPLATE, appName, TimeUnit.MILLISECONDS.toSeconds(elapsedTime),
                            TrackingUtils.getTrace(), commandStr));
                }
            }

            if (commandEvent instanceof CommandFailedEvent) {
                // fail alarm
                StringBuilder message = new StringBuilder();
                message.append(" ");
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ((CommandFailedEvent) commandEvent).getThrowable().printStackTrace(printWriter);
                StringBuffer error = stringWriter.getBuffer();
                message.append(error.toString());
                AlarmUtil.alarmTeams(String.format(EXCEPTION_COMMAND_TEMPLATE, appName, message.toString()));
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("mongo command result alarm exception: ", ex.getCause());
            }
        }
    }


    /**
     * command limit alarm
     * 
     * @param bson
     */
    public static void commandLimitAlarm(BsonDocument bson) {
        try {
            String appName = EnvUtil.getProperty("spring.application.name");
            if (EnvUtil.isUSA() || !EnvUtil.isAlarmService(appName)) {
                return;
            }

            if (bson.containsKey("insert")) {
                int mongoInsertsLimit = Integer.parseInt(EnvUtil.getProperty("mongo.alarm.inserts.limit", "-1"));
                BsonArray bsonArray = bson.getArray("documents");
                if (bsonArray != null && mongoInsertsLimit != -1 && bsonArray.size() > mongoInsertsLimit) {
                    String commandStr = CommandUtil.leftCommandStr(bson.toString());
                    AlarmUtil.alarmTeams(String.format(LIMIT_COMMAND_TEMPLATE, appName,
                            StringUtils.join("commandStr", ":", commandStr, " exceed mongo.alarm.inserts.limit", ":", mongoInsertsLimit)));
                }
            } else if (bson.containsKey("update")) {
                BsonArray bsonArray = bson.getArray("updates");
                int mongoUpdatesLimit = Integer.parseInt(EnvUtil.getProperty("mongo.alarm.updates.limit", "-1"));
                if (bsonArray != null && mongoUpdatesLimit != -1 && bsonArray.size() > mongoUpdatesLimit) {
                    String commandStr = CommandUtil.leftCommandStr(bson.toString());
                    AlarmUtil.alarmTeams(String.format(LIMIT_COMMAND_TEMPLATE, appName,
                            StringUtils.join("commandStr", ":", commandStr, " exceed mongo.alarm.updates.limit", ":", mongoUpdatesLimit)));
                }

                int mongoUpdateInLimit = Integer.parseInt(EnvUtil.getProperty("mongo.alarm.update.in.limit", "-1"));
                if (bsonArray.size() == 1 && mongoUpdateInLimit != -1) {// not support bulk update check
                    BsonDocument queryDocument = bsonArray.get(0).asDocument().getDocument("q");
                    if (queryDocument != null) {
                        Collection<BsonValue> bsonValues = queryDocument.values();
                        for (BsonValue bsonValue : bsonValues) {
                            if (mongoUpdateInLimit != -1 && bsonValue.isDocument() && Optional.ofNullable(((BsonDocument) bsonValue).get("$in"))
                                    .map(value -> ((BsonArray) value).size() > mongoUpdateInLimit).orElse(false)) {
                                String commandStr = CommandUtil.leftCommandStr(bson.toString());
                                AlarmUtil.alarmTeams(String.format(LIMIT_COMMAND_TEMPLATE, appName,
                                        StringUtils.join("commandStr", ":", commandStr, " exceed mongo.alarm.update.in", ":", mongoUpdateInLimit)));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("mongo command limit alarm exception: ", ex.getCause());
            }
        }
    }
}
