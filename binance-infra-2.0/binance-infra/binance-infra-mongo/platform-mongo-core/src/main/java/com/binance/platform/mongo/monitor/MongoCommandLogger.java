package com.binance.platform.mongo.monitor;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.mongodb.event.CommandEvent;
import com.mongodb.event.CommandFailedEvent;

public class MongoCommandLogger {

    private static final Logger logger = LoggerFactory.getLogger("monitor.mongo.sql");

    private static String LOG_TEMPLATE = "mongo request id:【 %s 】command耗时:【 %sS 】UUID:【%s】 \n %s";

    public static void doLog(long elapsedTime, CommandEvent commandEvent, String commandStr) {
        try {
            boolean monitorMongo = BooleanUtils.toBoolean(EnvUtil.getProperty("monitor.mongo.log", "false"));
            if (monitorMongo) {
                String logString = String.format(LOG_TEMPLATE, commandEvent.getRequestId(), TimeUnit.MILLISECONDS.toSeconds(elapsedTime),
                        TrackingUtils.getTrace(), commandStr);
                if (commandEvent instanceof CommandFailedEvent) {
                    logString = StringUtils.join(logString, " error:", ((CommandFailedEvent) commandEvent).getThrowable().getMessage());
                    logger.error(logString);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(logString);
                    } else {
                        logger.info(logString);
                    }
                }
            }
        } catch (Exception ex) {

        }
    }
}
