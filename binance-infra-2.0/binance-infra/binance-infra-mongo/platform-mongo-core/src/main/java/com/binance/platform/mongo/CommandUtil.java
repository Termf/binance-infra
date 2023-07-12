package com.binance.platform.mongo;

import com.binance.platform.env.EnvUtil;
import org.apache.commons.lang3.StringUtils;

public class CommandUtil {

    public static String leftCommandStr(String commandStr) {
        int commandLogMaxLen = Integer.parseInt(EnvUtil.getProperty("mongo.command.log.maxlen", "1000"));
        if (StringUtils.length(commandStr) > commandLogMaxLen) {
            return StringUtils.left(commandStr, commandLogMaxLen).concat("...");

        } else {
            return commandStr;
        }
    }
}
