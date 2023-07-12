package com.binance.master.task;

public final class TaskConstants {
    public static final String HANDLER_KEY = "TASK_HANDLER";
    public static final String HANDLER_PARAMS = "TASK_HANDLER_PARAMS";
    public static final String CMD_KEY = "TASK_CMD";

    public static enum CMD {
        BEAT,
        RUN,
        STOP
    }
}
