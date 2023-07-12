package com.binance.master.constant;

public final class MQConstant {

    public static final String MQ = "mq";

    /**
     * pnk-account 用户数据
     */
    public final static String ACCOUNT_PNK_USER_QUERY = "account_pnk_user_query";

    /**
     * account->pnk-admin 用户数据
     */
    public final static String ACCOUNT_PNK_ADMIN_USER_QUERY = "account_pnk_admin_user_query";

    /**
     * account->pnk的消息 用户数据
     */
    public final static String ACCOUNT_PNK_WEB_USER_QUERY = "account_pnk_web_user_query";
    
    /**
     * xx服务 ->exchange_admin的消息
     */
    public final static String RABBIT_TO_EXCHANGE_ADMIN_QUERY = "to_exchange_admin_query";

    /**
     * exchange: jumio自动审核审核通过
     */
    public static final String EX_ACCOUNT_JUMIO_PASS = "exchange.account.jumio.auto.pass";

    /**
     * queue: world-check 审核
     */
    public static final String QUEUE_WCK_APPLY = "queue.user.wck.apply";

    /**
     * IP黑名单
     */
    public static final String IP_BLACKLIST_QUEUE = "ip_black_list";
    
    public static final String DEPOSIT_DAEMON = "deposit_daemon_";

    /** 创建一个MQ的exchanges， 使用的是fanout模式 */
    public static final String JUMIO_RESULT_MQ_EXCHANGES = "jumio_result_notify";

    /** 重置流程创建一个MQ监听 JUMIO_RESULT_MQ_EXCHANGES 进行获取JUMIO的结果 */
    public static final String JUMIO_RESULT_RESET_QUEUE = "security_reset_jumio_queue";

    /** KYC 流程创建一个MQ监听 JUMIO_RESULT_MQ_EXCHANGES 进行获取JUMIO的结果 */
    public static final String JUMIO_RESET_KYC_QUEUE = "kyc_jumio_queue";

    /**
     * queue: 用户操作记录
     */
    public static final String USER_OPERATION_LOG_QUEUE = "user_operation_log";

    /**
     * report->pnk-admin 用户操作日志
     */
    public final static String REPORT_PNK_ADMIN_OPERATE_LOG_QUEUE = "report_pnk_admin_operate_log_queue";
    
    public static final String RISK_EXCHANGE = "risk_exception_action_exchange";
}
