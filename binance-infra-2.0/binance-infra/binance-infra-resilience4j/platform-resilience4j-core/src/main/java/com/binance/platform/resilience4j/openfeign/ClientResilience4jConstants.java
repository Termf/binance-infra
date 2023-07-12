package com.binance.platform.resilience4j.openfeign;

/**
 * author: sait xuc
 */
public class ClientResilience4jConstants {

    //crbreaker constants
    public static final int DEFAULT_FAIL_RATE = 50;//50%

    public static final long DEFAULT_WAIT_DURATION = 60;//60s

    public static final int DEFAULT_RB_CLOSED = 100;

    public static final int DEFAULT_RB_HALFOPEN = 10;

    //RateLimiter constants
    public static final int DEFAULT_LIMIT_FORPERIOD = 1000;

    public static final long DEFAULT_LIMIT_REFRESHPERIOD = 3;//3s

    //TimeLimit constants
    public static final long DEFAULT_TL_TIMEOUT = 1000;
    public static final boolean DEFAUL_TL_CANEL = true;

    public static final String RES4J_USEFLAG = "res4j_useflag";

    public static final String VALUE_UFLAG = "value";

    public static final String KEY_UFLAG = "key";

}
