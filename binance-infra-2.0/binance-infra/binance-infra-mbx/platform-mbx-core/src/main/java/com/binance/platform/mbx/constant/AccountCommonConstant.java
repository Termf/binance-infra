package com.binance.platform.mbx.constant;

import java.util.Arrays;
import java.util.List;

public final class AccountCommonConstant {

    public static final String LOCAL_IP = "local-ip";

    public static final String BINANCE_COM = "https://www.binance.com/";

    public static final String SESSION_INFO_KEY = "SESSION_INFO_KEY";
    public static final String HEADER_USER_ID = "X-USER-ID";
    public static final String HEADER_USER_EMAIL = "X-USER-EMAIL";
    public static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    public static final String HEADER_ORIGIN_FORWARDED_FOR = "X-Origin-Forwarded-For";

    public static final String LANG = "lang";

    // common header
    public static final String HEADER_TOKEN = "x-token";
    public static final String HEADER_CLIENT_TYPE = "clienttype";
    public static final String HEADER_LANG = "lang";
    public static final String HEADER_AD_BLOCK = "x-ad-block";
    public static final String HEADER_CSRFTOKEN = "csrftoken";
    public static final String HEADER_DEVICE_INFO = "device-info";
    public static final String HEADER_DEVICE_INFO_PNK = "device_info";


    // 仅登录未做2FA
    public static final String PRE_AUTH_KEY = "PRE_AUTH_KEY";

    public static final String TOKEN_SECOND_VERIFY = "verify";

    public static final String COOKIE_TOKEN = "p20t";
    public static final String COOKIE_TOKEN_V2 = "p20t2";
    public static final String COOKIE_MIX_TOKEN1 = "d1og";
    public static final String COOKIE_MIX_TOKEN2 = "r2o1";
    public static final String COOKIE_MIX_TOKEN3 = "f30l";
    public static final String COOKIE_SERIAL_NO = "s9r1";
    public static final String COOKIE_CSRFTOKEN = "CSRFToken";
    public static final String COOKIE_NEW_CSRFTOKEN = "cr00";
    public static final String COOKIE_NEW_CSRFTOKEN_V2 = "cr002";
    public static final String COOKIE_DEVICE_AUTH_CODE = "d91f";
    // authcenter 真正的值
    public static final String AUTHCENTER_TOKEN = "token";
    public static final String AUTHCENTER_SERIAL_NO = "serialNo";
    public static final String JSESSIONID = "JSESSIONID";

    public static final String COOKIE_CLIENT_ID = "clientId";

    // 退出时需要清理的cookie
    public static final List<String> CLEAR_COOKIE_LIST = Arrays.asList(COOKIE_TOKEN, COOKIE_MIX_TOKEN1,
            COOKIE_MIX_TOKEN2, COOKIE_MIX_TOKEN3, COOKIE_SERIAL_NO, COOKIE_NEW_CSRFTOKEN);

    // 退出时需要清理的老pnk系统中的cookie
    public static final List<String> CLEAR_OLD_COOKIE_LIST = Arrays.asList(COOKIE_CSRFTOKEN, JSESSIONID);

    public static final String API_REQUEST_HEADER = "api_request_header";

    public static final String LOGIN_SESSION = "login_session";

    public static final String MATCHBOX_WEB_SERVICE = "matchbox";// 撮合服务

    public static final String MESSAGE_WEB_SERVICE = "message";// 服务

    public static final String ACCOUNT_WEB_SERVICE = "account";// 服务

    public static final String INBOX_WEB_SERVICE = "inbox";// 站内信服务

    public static final String CAPITAL_WEB_INSPECTOR = "inspector";// 第三方身份验证服务

    public static final String REPORT_WEB_SERVICE = "report";//报表服务

    public static final String NOTIFICATION_WEB_SERVICE = "notification";// 后台推送服务

    public static final String RISK_WEB_SERVICE = "risk-management";// 风控服务

    public static final String DECISION_ENGINE_SERVICE = "decision-engine";//决策引擎服务

    public static final String CAPITAL_WEB_SERVICE = "capital-web";//出入金

    public static final String BASE_URL = "base-url";// url

    public static final String BASE_BROWER = "base-brower";

    public static final String IP_ADDRESS = "ip-address";


    public static final String MBX_GATEWAY_SERVICE = "mbx-gateway"; // MBX Gateway
    public static final String ASSET_SERVICE = "asset-service";
    public static final String NUMBERING_RULE_SERVICE = "numbering-rule-service";

    // 用户状态
    // 用户状态：使用long类型的2进制占位可以有64个状态统一0:否1:是,1:激活状态,2:禁用状态,3:锁定状态,4:特殊用户,5:种子用户,6:协议是否确认,
    // 7:手机验证,8:强制修改密码,9:是否有子账号,10:是否可以申购,11:是否可以交易,12:微信绑定,13:APP是否可以交易,14:api是否可以交易,,15:BNB手续费开关
    public static final long USER_ACTIVE = 1L;// 激活
    public static final long USER_DISABLED = 2L << 0;// 禁用
    public static final long USER_LOCK = 2L << 1;// 锁定
    public static final long USER_SPECIAL = 2L << 2;// 特殊用户
    public static final long USER_SEND = 2L << 3;// 种子用户
    public static final long USER_PROTOCOL = 2L << 4;// 协议确认
    public static final long USER_MOBILE = 2L << 5;// 是否需要手机验证
    public static final long USER_FORCED_PASSWORD = 2L << 6;// 强制修改密码
    public static final long USER_SUB_ACCOUNT = 2L << 7;// 是否是子账号(弃用!)
    public static final long USER_IS_SUBUSER = 2L << 7;// 是否是子账号
    public static final long USER_PURCHASE = 2L << 8;// 申购
    public static final long USER_TRADE = 2L << 9;// 交易
    public static final long USER_WEIXIN = 2L << 10;// 微信绑定(弃用!)
    public static final long USER_IS_SUBUSER_FUNCTION_ENABLED = 2L << 10;// 是否拥有子账号功能
    public static final long USER_TRADE_APP = 2L << 11;// app交易
    public static final long USER_TRADE_API = 2L << 12;// api交易
    public static final long USER_FEE = 2L << 13;// BNB手续费开关
    public static final long USER_GOOGLE = 2L << 14;// 谷歌2次验证 0 未认证 1 已认证
    public static final long USER_CERTIFICATION = 2L << 15;// 用户认证 0 未认证 1 已认证
    public static final long USER_CERTIFICATION_TYPE = 2L << 16;// 用户认证类型 0 个人 1企业
    public static final long USER_WITHDRAW_WHITE = 2L << 17;// 出入金白名单 0 未开启 1 开启
    public static final long USER_DELETE = 2L << 18;// 删除用户 0 未开启 1 开启
    public static final long USER_IS_SUB_USER_ENABLED = 2L << 19;// 是否启用子账户 0 未开启 1 开启
    public static final long USER_IS_MARGIN_USER = 2L << 20;// 是否是margin_user 0 不是 1 是
    public static final long USER_IS_EXIST_MARGIN_ACCOUNT = 2L << 21;// 该账户是否拥有margin账户 0 不是 1 是
    public static final long USER_IS_FUTURE_USER = 2L << 22;// 是否是future_user 0 不是 1 是
    public static final long USER_IS_EXIST_FUTURE_ACCOUNT = 2L << 23;// 该账户是否拥有future账户 0 不是 1 是
    public static final long USER_QUESTION = 2L << 24;// 用户是否答题 0 不是 1 是
    public static final long USER_LOGIN = 2L << 25;// 登录权限
    public static final long USER_IS_BROKER_SUBUSER_FUNCTION_ENABLED = 2L << 26;// 是否拥有broker子账户功能
    public static final long USER_IS_BROKER_SUBUSER = 2L << 27;// 是否是broker子账户
    public static final long USER_IS_BROKER_SUB_USER_ENABLED = 2L << 28;// 是否启用broker子账户 0 未开启 1 开启

    public static final long USER_IS_FIAT_USER = 2L << 29;// 是否是fiat_user 0 不是 1 是
    public static final long USER_IS_EXIST_FIAT_ACCOUNT = 2L << 30;// 该账户是否拥有fiat账户 0 不是 1 是
    public static final long DISABLE_FUTURE_INTERNAL_TRANSFER = 2L << 31;// 是否禁止期货内部划转 0 允许 1 禁止
    public static final long FIAT_PROTOCOL_CONFIRM = 2L << 32;// 是否已签署法币账户开通协议 0 不是 1 是
    public static final long USER_FAST_WITHDRAW_ENABLED = 2L << 33;// 是否开通快速提币 0 未开启 1 开启

    public static final long USER_IS_ASSET_SUBUSER_FUNCTION_ENABLED = 2L << 34;// 是否拥有资管子账户功能
    public static final long USER_IS_ASSET_SUBUSER = 2L << 35;// 是否是资管子账户
    public static final long USER_IS_ASSET_SUB_USER_ENABLED = 2L << 36;// 是否启用资管子账户 0 未开启 1 开启
    public static final long USER_TRADE_COMMISSION_ENABLED = 2L << 37;// 是否满足交易返佣的资格 0 不满足 1 已满足，针对美国站合规使用
    public static final long USER_IS_MINING_USER = 2L << 40;// 是否是mining_user 0 不是 1 是
    public static final long USER_IS_EXIST_MINING_ACCOUNT = 2L << 41;// 该账户是否拥有mining账户 0 不是 1 是


    public static final long FORBIDDEN_BROKER_TRANSFER = 2L << 39;// 是否禁止broker划转 0 不禁止 1 禁止

    public static final long SIGNED_LVT_RISK_AGREEMENT = 2L << 47;// 是否签署leverage token风险协议 0 未签署 1 已签署


    public static final long USER_FUND_PASSWORD = 2L << 42;// 是否启用资金密码 0 未开启 1 开启




    public static final long USER_IS_ISOLATED_MARGIN_USER = 2L << 43;// 是否是逐仓margin账户 0 不是 1 是
    public static final long USER_IS_EXIST_ISOLATED_MARGIN_ACCOUNT = 2L << 44;// 该账户是否拥有逐仓margin账户 0 不是 1 是


    public static final long USER_IS_MOBILE_USER = 2L << 45;// 该用户是否是手机用户 0 不是 1 是

    public static final long USER_NOT_BIND_EMAIL = 2L << 46;// 该用户是否是没有绑定邮箱 0 代表绑定，1 代表没绑定邮箱

    public static final long USER_IS_CARD_USER = 2L << 49;// 是否是card_user 0 不是 1 是
    public static final long USER_IS_EXIST_CARD_ACCOUNT = 2L << 50;// 该账户是否拥有card账户 0 不是 1 是
    public static final long ONE_BUTTON_REGISTER_USER = 2L << 48;// 是否是一键注册用户 0 不是，1 是

    public static final long USER_IS_WAAS_USER = 2L << 51;// 是否是waas_user 0 不是 1 是




    /*----------------------------message消息节点类型--------------------------------*/
    /**
     * 消息节点类型: 邮箱找密码
     */
    public static final String NODE_TYPE_EMAIL_PWD = "email_pwd";
    public static final String NODE_TYPE_TML_EMAIL_PWD = "email_tml_pwd";
    public static final String NODE_TYPE_RESET_PASSWORD = "email_pwd_success";// 重置密码成功邮件
    public static final String NODE_TYPE_THIRD_PARTY_RESET_PASSWORD = "thirdparty_email_pwd";// 第三方注册用户，在自动注册时发送重置密码邮件
    
    /**
     * 消息节点类型: GAUTH
     */
    public static final String NODE_TYPE_EMAIL_GAUTH = "email_gauth";
    /**
     * 消息节点类型: 手机注册
     */
    public static final String NODE_TYPE_MOBILE_REGISTER = "register_by_mobile";
    public static final String NODE_TYPE_REGISTER_REMIND = "register_remind_msg";
    /**
     * 消息节点类型: 手机注册
     */
    public static final String NODE_TYPE_REGISTER_PASSWORD = "register_password_msg";
    public static final String NODE_TYPE_MOBILE_BINDCARD = "bindcard_by_mobile";
    public static final String NODE_TYPE_MOBILE_MODIFYCARD = "modifycard_by_mobile";
    public static final String NODE_TYPE_MOBILE_CHECK = "bank_mobile_check";
    /**
     * 消息节点类型: 手机短信登录
     */
    public static final String NODE_TYPE_MOBILE_LOGIN = "login_by_mobile";
    /**
     * 消息节点类型: 手机找密码
     */
    public static final String NODE_TYPE_MOBILE_PWD = "mobile_pwd";
    /**
     * 消息节点类型: 手机重置支付密码
     */
    public static final String NODE_TYPE_MOBILE_RESET_PAY_PWD = "mobile_reset_pay_pwd";
    /**
     * 消息节点类型: 解绑手机
     */
    public static final String NODE_TYPE_MOBILE_UPDATE = "mobile_update";
    /**
     * 消息节点类型: 绑定手机
     */
    public static final String NODE_TYPE_MOBILE_BIND = "mobile_bind";
    /**
     * 消息节点类型: 登录提示
     */
    public static final String NODE_TYPE_LOGIN_REMIND = "login_remind_msg";
    /**
     * 消息节点类型: 取现验证码
     */
    public static final String NODE_TYPE_WITHDRAW_REMIND = "withdraw_remind";
    /**
     * 消息节点类型: 手机验证
     */
    public static final String NODE_TYPE_MOBILE_VERIFY = "mobile_verify";
    /**
     * 消息节点类型: sdk入金手机验证
     */
    public static final String NODE_TYPE_SDK_SAVECHARGE_VERIFY = "sdk_saveCharge_verify";
    /**
     * 消息节点类型: sdk出金手机验证
     */
    public static final String NODE_TYPE_SDK_WITHDRAW_VERIFY = "sdk_withDraw_verify";
    /**
     * 消息节点类型: 激活邮箱
     */
    public static final String NODE_TYPE_EMAIL_AUTH = "email_auth";
    /**
     * 消息节点类型: oauth激活opendid
     */
    public static final String NODE_TYPE_OAUTH_LOGIN = "email_binancelogin";
    /**
     * 消息节点类型: oauth激活opendid
     */
    public static final String NODE_TYPE_OAUTH_LOGIN_SIGNUP = "email_binancelogin_signup";
    /**
     * 消息节点类型: 激活手机 终端激活码
     */
    public static final String NODE_TYPE_EMAIL_TERMINAL_AUTH = "email_tml_auth";
    /**
     * 消息节点类型: 登陆ip提醒
     */
    public static final String NODE_TYPE_EMAIL_IP = "email_login_ip";
    /**
     * 消息节点类型: 登陆ip短信提醒
     */
    public static final String NODE_TYPE_SMS_IP = "sms_login_ip";
    /**
     * 消息节点类型: 绑定二次验证
     */
    public static final String NODE_TYPE_BIND_GOOGLE = "bind_google";
    /**
     * 消息节点类型: 解绑二次验证
     */
    public static final String NODE_TYPE_UNBIND_GOOGLE = "unbind_google";
    /**
     * 消息节点类型: 开启关闭二次验证
     */
    public static final String NODE_TYPE_SET_GOOGLE_STATUS = "set_google_status";
    /**
     * 消息节点类型: 激活邮箱 主题
     */
    public static final String EMAIL_SUBJECT_EMAIL_AUTH = "激活邮箱邮件";
    /**
     * 消息节点类型: 提货申请
     */
    public static final String NODE_TYPE_PRODUCT_WITHDRAW_APPLY = "product_withdraw_apply";
    /**
     * 验证码状态：有效
     */
    public static final String VERIFY_CODE_STATUS_VALID = "valid";
    /**
     * 验证码状态：无效
     */
    public static final String VERIFY_CODE_STATUS_INVALID = "invalid";
    /**
     * 消息节点类型: 提货申请
     */
    public static final String NODE_TYPE_ASSET_WITHDRAW_APPLY = "asset_withdraw_apply";
    /**
     * 消息节点类型: 谷歌验证解绑
     */
    public static final String NODE_TYPE_GOOGLE_VERIFY_UNBIND = "google_verify_unbind";
    /**
     * 消息节点类型: 手机解绑
     */
    public static final String NODE_TYPE_MOBILE_UNBIND = "mobile_unbind";
    /**
     * 消息节点类型: API创建
     */
    public static final String NODE_TYPE_API_CREATE = "api_create_enable";
    /**
     * 消息节点类型: API修改
     */
    public static final String NODE_TYPE_API_MODIFY = "api_modify";
    /**
     * 消息节点类型: API提现确认
     */
    public static final String NODE_TYPE_API_WITHDRAW_ENABLE = "api_withdraw_enable";
    /**
     * 消息节点类型: 新增出金地址白名单
     */
    public static final String NODE_TYPE_WITHDRAW_WHITE_LIST_SAVE = "withdraw_save_white_list";
    /**
     * 消息节点类型: 关闭出金地址白名单功能
     */
    public static final String NODE_TYPE_WITHDRAW_WHITE_LIST_CLOSE = "withdraw_close_white_list";
    public static final String NODE_TYPE_TABLE_ANTI_PHISHING = "table_anti_phishing";
    /**
     * 邮件消息: 新设备授权
     */
    public static final String NODE_TYPE_DEVICE_AUTHORIZE = "email_new_device_authorize";
    /**
     * 邮件消息: 新设备登录提醒
     */
    public static final String NODE_TYPE_DEVICE_REMIND = "email_new_device_remind";
    // 大户邮寄确定
    public static final String USER_IP_CHANGE_CONFIRM = "user_ip_change_confirm";
    /**
     * 邮件消息: 重复注册
     */
    public static final String NODE_TYPE_DUPLICATE_REGISTRATION = "node_type_duplicate_registration";
    /**
     * 短信通知: 由于提币人脸人脸识别检测拒绝KYC的短信通知
     */
    public static final String SMS_WITHDRAW_FACE_KYC_REFUSED = "sms_withdraw_face_kyc_refused";
    /**
     * 短信通知：由于提币人脸识别检测中用户没有做过人脸KYC的短信通知
     */
    public static final String SMS_WITHDRAW_FACE_KYC_NOTIFY = "sms_withdraw_face_kyc_notify";
    /**
     * 邮件消息：由于提币人脸识别检测中用户没有做过人脸KYC的邮件通知
     */
    public static final String EMAIL_WITHDRAW_FACE_KYC_NOTIFY = "email_withdraw_Face_kyc_notify";

    /**
     * 短信通知：由于提币人脸触发的通知短信
     */
    public static final String SMS_WITHDRAW_FACE_NOTIFY = "sms_withdraw_face_notify";

    public static final String AUTO_SEND_TEMPLATE_MAIL_CHARGE = "mail_template_charge";
    public static final String AUTO_SEND_TEMPLATE_SMS_CHARGE = "sms_template_charge";

    public static final String BLOCK_CHAIN_FORK_NOTICE = "block_chain_fork_notice";


    /**
     * 邮件消息: 重置2FA流程通知用户做人脸识别
     */
    public static final String USER_2FA_RESET_FACE_NOTIFY = "email_2fa_reset_face_notify";
    public static final String RESET_EMAIL_2FA_SUCCESS = "2fa_reset_success";
    public static final String RESET_EMAIL_2FA_FAIL = "2fa_reset_failure";
    public static final String RESET_EMAIL_ENABLE_SUCCESS = "user_enable_success";
    public static final String RESET_EMAIL_ENABLE_FAIL = "user_enable_failure";


    /**
     * 邮件消息: jumio审核
     */
    public static final String JUMIO_KYC_CHECK_SUCCESS = "jumio_kyc_check_success";
    public static final String JUMIO_KYC_CHECK_FAIL = "jumio_kyc_check_fail";
    public static final String JUMIO_COMPANY_CHECK_SUCCESS = "jumio_company_check_success";
    public static final String JUMIO_COMPANY_CHECK_FAIL = "jumio_company_check_fail";

    /**
     * 模板属性:验证码
     */
    public static final String MESSAGE_TEMPLATE_PROP_VERIFYCODE = "verifyCode";
    public static final String MESSAGE_TEMPLATE_PROP_REALNAME = "realName";
    public static final String MESSAGE_TEMPLATE_PROP_INVITECODE = "inviteCode";

    /**
     * 激活Yubikey邮件
     */
    public static final String MESSAGE_TEMPLATE_ACTIVATE_YUBIKEY = "turn_on_yubikey";


    /*----------------------------Security Log operate_type类型--------------------------------*/
    public static final String SECURITY_OPERATE_TYPE_LOGIN = "login";
    public static final String SECURITY_OPERATE_TYPE_REGIST = "regist";
    public static final String SECURITY_OPERATE_TYPE_UPDATE_PASSWORD = "update_password";
    public static final String SECURITY_OPERATE_TYPE_RESET_PASSWORD = "reset_password";
    public static final String SECURITY_OPERATE_TYPE_BIND_MOBILE = "bind_mobile";
    public static final String SECURITY_OPERATE_TYPE_BIND_GOOGLE = "bind_google";
    public static final String SECURITY_OPERATE_TYPE_UNBIND_MOBILE = "unbind_mobile";
    public static final String SECURITY_OPERATE_TYPE_UNBIND_GOOGLE = "unbind_google";
    public static final String SECURITY_OPERATE_TYPE_UPDATE_ACCOUNT = "update_account";
    public static final String SECURITY_OPERATE_TYPE_ACCOUNT_ACTIVE = "account_active";
    public static final String SECURITY_OPERATE_TYPE_FORGOT_PASSWORD = "forgot_password";
    public static final String SECURITY_OPERATE_TYPE_OPEN_MOBILE_VERIFY = "open_mobile_verify";
    public static final String SECURITY_OPERATE_TYPE_CLOSE_MOBILE_VERIFY = "close_mobile_verify";
    public static final String SECURITY_OPERATE_TYPE_OPEN_BNB_FEE = "open_bnb_fee";
    public static final String SECURITY_OPERATE_TYPE_CLOSE_BNB_FEE = "close_bnb_fee";
    public static final String SECURITY_OPERATE_TYPE_UPDATE_MARGIN_INTEREST_BNB = "update_margin_interest_bnb_fee";
    public static final String SECURITY_OPEN_WITHDRAW_WHITE_STATUS = "open_withdraw_white_status";
    public static final String SECURITY_CLOSE_WITHDRAW_WHITE_STATUS = "close_withdraw_white_status";
    public static final String SECURITY_OPERATE_TYPE_BIND_PHISHING_CODE = "bind_phishing_code";
    public static final String SECURITY_OPERATE_TYPE_UPDATE_AGENT_REWARD_RATIO = "update_agent_reward_ratio";
    public static final String SECURITY_OPERATE_TYPE_FORBIDDEN_USER = "forbidden_user";
    public static final String SECURITY_OPERATE_TYPE_LOCK_USER = "lock_user";
    public static final String SECURITY_OPERATE_TYPE_SECURITY_LEVEL_SETTING = "security_level_setting";
    public static final String SECURITY_OPERATE_TYPE_APP_FORGET_PSW = "app_forget_psw";
    public static final String SECURITY_OPERATE_TYPE_APP_RESET_PSW = "app_reset_psw";
    public static final String SECURITY_OPERATE_TYPE_MODIFY_USER = "modify_user";
    public static final String SECURITY_OPERATE_TYPE_DELETE_USER = "delete_user";
    public static final String SECURITY_OPERATE_REVIEW_SECURITY_RESET = "review_security_reset";
    public static final String SECURITY_OPERATE_ONE_BUTTON_ACTIVATION = "one_button_activation";
    public static final String SECURITY_OPERATE_ONE_BUTTON_DISABLE = "one_button_disable";
    public static final String SECURITY_OPERATE_AUTHORIZE_DEVICE = "authorize_device";
    public static final String SECURITY_OPERATE_DELETE_DEVICE = "delete_authorized_device";
    public static final String SECURITY_OPERATE_TYPE_FORGET_PSW = "forget_psw";
    public static final String SECURITY_OPERATE_ENABLED_SUBUSER = "enabled_sub_user";
    public static final String SECURITY_OPERATE_DISABLE_SUBUSER = "disable_sub_user";
    public static final String SECURITY_OPERATE_RESET_GOOGLE = "reset_sub_user_google";
    public static final String SECURITY_OPERATE_RESET_MOBILE = "reset_sub_user_mobile";
    public static final String SECURITY_OPERATE_UPDATE_SUBUSER_PSW = "update_sub_user_psw";
    public static final String SECURITY_OPERATE_DISABLE_LOGIN = "disable_login";
    public static final String SECURITY_OPERATE_ACTIVE_LOGIN = "active_login";

    /*------------------------------------ exchange Constants  ----------------------------------------*/
    // exchange setting
    public static final String EXCHANGE_AGNET = "exch_agent";
    public static final String DEFAULT_AGNET = "default_agent";
    public static final String EXCHANGE_AGNET_RATIO = "exch_agent_ratio";
    public static final String EXCHANGE_CLEARING_RATIO = "exch_clearing_ratio";
    public static final String VENDOR_CLEARING_RATIO = "vendor_clearing_ratio";
    public static final String EXCHANGE_CLEARING_USER = "exch_clearing_user";
    public static final String VENDOR_CLEARING_USER = "vendor_clearing_user";

    // default currency
    public static final String DEFAULT_CURRENCY = "CNY";

    /**
     * 流水相关类型常量
     */
    ///////////////////////////////////////////////////////////////////
    // 初始金额
    ///////////////////////////////////////////////////////////////////
    public static final int INIT_MONEY = 0;

    ///////////////////////////////////////////////////////////////////
    // 交易相关
    ///////////////////////////////////////////////////////////////////
    public static final int MONEY_TRADING = 1;
    public static final int ASSET_TRADING = 2;

    public static final int USER_CHARGE = 11;

    // 提货
    public static final int WITHDRAW = 32;
    // 提货失败
    public static final int WITHDRAW_FAIL = 33;
    // 提货取消
    public static final int WITHDRAW_CANCEL = 34;
    // 提货成功
    public static final int WITHDRAW_SUCCESS = 35;
    // 提货回滚
    public static final int WITHDRAW_RESET = 36;

    public static final int BUY_POINT = 91;
    public static final int SHOP_TRADE = 92;

    public static final int FLUSH_COMMISSION = 102;

    /**
     * 常用时间（秒）
     */
    public static final int DAY = 3600 * 24;
    public static final int HOUR = 3600;
    public static final int HOUR_HALF = 1800;
    public static final int MINUTE_10 = 600;
    public static final int MINUTE_5 = 300;

    /**
     * api constants
     */
    public static final String SUCCESS = "success";
    public static final String OK = "ok";
    public static final String FAIL = "fail";
    public static final String ERROR = "error";
    public static final String UNKNOWN = "unknown";

    /** 文件的读取模式，sys_config中配置的key 值: FTP S3 */
    public static final String CONFIG_FILE_STORAGE_READ = "file_storage_read";
    /** sys_config file_storage_read 的value 值, 从 FTP 中读取 */
    public static final String CONFIG_FILE_STORAGE_READ_FTP = "FTP";
    /** sys_config file_storage_read 的value 值, 从 S3 中读取 */
    public static final String CONFIG_FILE_STORAGE_READ_S3 = "S3";
    /** sys_config 配置值, 是否写入到 FTP 中, 值: true/false */
    public static final String CONFIG_FILE_STORAGE_WRITE_FTP = "file_storage_write_ftp";
    /** sys_config 配置值, 是否写入到 S3 中, 值: true/false */
    public static final String CONFIG_FILE_STORAGE_WRITE_S3 = "file_storage_write_s3";



    /**
     * 灰度环境
     */
    public static final String GRAY_ENV_HEADER = "X-GRAY-ENV";
    // gateway中设置为灰度环境的标志，若是灰度环境eureka.instance.metadataMap.envflag也要设置为gray
    public static final String ENV_FLAG_GRAY = "gray";
    // 正常环境为normal
    public static final String ENV_FLAG_NORMAL = "normal";
    // 查找metadataMap中的属性，固定为envflag
    public static final String ENVFLAG = "envflag";





}