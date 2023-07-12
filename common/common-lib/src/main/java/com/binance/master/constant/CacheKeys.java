package com.binance.master.constant;

public final class CacheKeys {

	public final static String REDIS_LOCK = "REDIS_LOCK";

	public final static String REGISTER_USER_ID = "REGISTER_USER_ID";//用户注册id

	public final static String REGISTER_EMAIL = "REGISTER_EMAIL";

	public final static String OAUTH_LINK_EMAIL = "OAUTH_LINK_EMAIL";

	public final static String RESET_PASSWORD_EMAIL = "RESET_PASSWORD_EMAIL";

    public final static String APP_RESET_PASSWORD_EMAIL = "APP_RESET_PASSWORD_EMAIL";

    public final static String MOBILE_AUTH_TIME = "MOBILE_AUTH_TIME";

	public final static String USER_DISABLE_CODE = "USER_DISABLE_CODE";//一键禁用码前缀

	public final static String USER_WITHDRAW_WHITE = "USER_WITHDRAW_WHITE";//用户关闭出币白名单前缀

	public final static String MOBILE_AUTH = "MOBILE_AUTH";//手机认证码前缀

	public final static String OTHER_CIPHER = "OTHER_CIPHER";//其他加密使用

	public final static String PASSWORD_CIPHER = "PASSWORD_CIPHER";//密码加密使用

	public final static String SECURITY_CIPHER = "SECURITY_CIPHER";//安全加密盐

	public final static String SECURITY_CIPHER_ENV = "SECURITY_CIPHER_ENV";//安全加密盐

	public final static String ROUTING_SERVER = "ROUTING_SERVER";

	public final static String ACCOUNT_SERVER = "ACCOUNT_SERVER";

	public final static String REGISTER_EMAIL_CODE = "REGISTER_EMAIL_CODE";

	public final static String OLD_SYS_CONFIG = "OLD_SYS_CONFIG";

	public final static String USER_ID_GENERATE_LOCK = "USER_ID_GENERATE";

	public final static String USER_SYNCHRON_LOCK = "USER_SYNCHRON_LOCK";

	public final static String USER_SYNCHRON_TASK_INSERT_LOCK = "USER_SYNCHRON_TASK_INSERT_LOCK";

	public final static String USER_SECURITY_INFO = "USER_SECURITY_INFO";

	public final static String USER_LOGIN_IP = "USER_LOGIN_IP";

	public final static String USER_KYC_RESULT_JOB_LOCK = "USER_KYC_RESULT_JOB_LOCK";

	public final static String USER_KYC_PHOTO_JOB_LOCK = "USER_KYC_PHOTO_JOB_LOCK";

	public final static String USER_KYC_FAIL_JOB_LOCK = "USER_KYC_FAIL_JOB_LOCK";

	public final static String USER_KYC_RESCAN_JOB_LOCK = "USER_KYC_RESCAN_JOB_LOCK";

	public final static String USER_KYC_RESCAN_HANDLE_JOB_LOCK = "USER_KYC_RESCAN_HANDLE_JOB_LOCK";

	public final static String REGISTER_IP_COUNT = "REGISTER_IP_COUNT";

	public final static String DUPLICATE_REGISTER_EMAIL_COUNT = "DUPLICATE_REGISTER_EMAIL_COUNT";

	//redis lock prefix
	public final static String USER_WITHDRAW_LOCK_PREFIX = "USER_WITHDRAW_LOCK_";
	
	public final static String BLOCKCHAIN_DATA_LOCK_PREFIX = "BLOCKCHAIN_DATA_LOCK_";
	
	public final static String DEPOSIT_DAEMON_LOCK_PREFIX = "DEPOSIT_DAEMON_LOCK";

	//zset key of locked user emails
	public final static String LOCKED_USER_EMAIL_ZSET = "LOCKED_USER_EMAIL_ZSET";

	//ftp to s3
	public final static String FTP_TO_S3_TRANSFER_QUEUE = "FTP_TO_S3_TRANSFER_QUEUE";

	public final static String FTP_TO_S3_TRANSFER_FAILED_QUEUE = "FTP_TO_S3_TRANSFER_FAILED_QUEUE";

	public final static String FTP_TO_S3_TRANSFER_LAST_TIMESTAMP = "FTP_TO_S3_TRANSFER_LAST_TIMESTAMP";

	// jumio result process lock prefix
	public final static String JUMIO_RESULT_PROCESS_LOCK = "JUMIO_RESULT_PROCESS_LOCK_";
	// 批量重跑JUMIO 规则锁
	public final static String JUMIO_BATCH_REDO_RULE_LOCK = "JUMIO_BATCH_REDO_RULE_LOCK";
	
	public final static String USER_CONFIG_CODE = "USER_CONFIG_CODE";

	// 用户初始化提交KYC的锁，防止多次提交
	public final static String USER_KYC_INIT_LOCK = "USER_KYC_INIT_LOCK_";
	public final static String COMPANY_KYC_INIT_LOCK = "COMPANY_KYC_INIT_LOCK_";
}