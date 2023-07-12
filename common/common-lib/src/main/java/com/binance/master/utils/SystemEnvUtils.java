package com.binance.master.utils;

/**
 * 系统环境工具
 * Created by Shining.Cai on 2018/08/22.
 **/
public class SystemEnvUtils {

    public static final String PROD = "prod";
    public static final String QA = "qa";
    public static final String DEV = "dev";
    public static final String LOCAL = "local";


    private static String env;

    static {
        env = System.getenv("TARGET_ENV");
    }

    /**
     * 获取当前环境
     */
    public static String getEnv(){
        return env;
    }

    public static boolean isEqual(String srcEnv){
        return env.equals(srcEnv);
    }

    public static boolean isProd(){
        return PROD.equalsIgnoreCase(env);
    }

    public static boolean isLocal(){
        return LOCAL.equalsIgnoreCase(env);
    }

    public static boolean isDev(){
        return DEV.equalsIgnoreCase(env);
    }

    public static boolean isQa(){
        return QA.equalsIgnoreCase(env);
    }

}
