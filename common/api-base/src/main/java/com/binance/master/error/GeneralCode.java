package com.binance.master.error;


import org.apache.commons.lang3.StringUtils;

/**
 * 系统基本公共异常
 *
 * @author wang-bijie
 */
public enum GeneralCode implements ErrorCode {
    // 100系统异常
    // 001业务代码
    // 001明细代码
    SUCCESS("000000000", "正常"),
    SYS_ERROR("100001001", "系统异常"),
    SYS_NOT_SUPPORT("100001002", "不支持的操作"),
    SYS_VALID("100001003", "验证不通过"),
    SYS_ACCESS_LIMITED("100001004", "访问受到限制"),
    SYS_NOT_LOGIN("100001005", "需要登录才能访问"),
    SYS_NOT_EXIST("100001006", "不存在记录"),
    SYS_NOT_VALID_TYPE("100001007", "不支持的验证类型"),
    SYS_BODY_NULL("100001008", "业务主体为空"),
    SYS_ZUUL_ERROR("100001009", "系统正忙，请稍后再试！"),
    SYS_MAINTENANCE("100001010", "系统维护中"),
    SOCKET_TIMEOUT("100001011", "接口调用超时"),
    TOKEN_EXPIRE("100002001", "token过期"),
    TOKEN_NOT_EXIST("100002002", "无效的token"),
    GET_REDIS_ERROR("100002003", "获取redis对象异常"),
    GET_REDIS_LOCKED_FAILED("100002004", "获取redis锁失败"),

    COMMON_ERROR("200000000", "一般错误"),
    USER_EXIST("200001001", "用户已经存在"),
    USER_ID_ZERO("200001002", "用户id已经用尽"),
    USER_NOT_EXIST("200001003", "用户不存在"),
    USER_PWD_ERROR("200001004", "密码错误"),
    USER_NOT_ACTIVE("200001005", "账号未激活"),
    USER_ACTIVE_CODE_EXPIRED("200001006", "激活码已经过期"),
    USER_ACTIVE_CODE_ERROR("200001007", "激活码错误"),
    USER_ACTIVE_ERROR("200001008", "激活错误"),
    USER_ALREADY_ACTIVATED("200001009", "已经激活"),
    USER_DISABLED("200001010", "账号已经被禁止使用"),
    USER_LOCK("200001011", "账号已经被锁定"),
    USER_EMAIL_USE("200001012", "邮箱已经被使用"),
    USER_2FA_CODE_ERROR("200001013", "2fa验证码错误"),
    USER_NOT_MOBILE("200001014", "用户没有绑定手机"),
    USER_MOBILE_AUTH_CODE_EXPIRED("200001015", "用户手机认证码已经过期"),
    USER_MOBILE_AUTH_CODE_ERROR("200001016", "用户手机认证码错误"),
    USER_MOBILE_EXIST("200001017", "用户手机已经存在"),
    USER_CERTIFICATE_AUDIT("200001018", "个人身份审核中"),
    USER_CERTIFICATE_PASS("200001019", "个人身份审核通过"),
    USER_CERTIFICATE_USE("200001020", "证件号码已经被使用"),
    USER_CERTIFICATE_REFUSAL("200001021", "身份审核拒绝"),
    USER_COMPANY_AUDIT("200001022", "企业审核中"),
    USER_COMPANY_PASS("200001023", "企业审核通过"),
    USER_COMPANY_USE("200001024", "企业已经被使用"),
    USER_COMPANY_REFUSAL("200001025", "企业审核拒绝"),
    USER_AUDIT_NOT_EXIST("200001026", "审核信息不存在"),
    USER_MOBILE_AUTH_CODE_FILL("200001027", "必须填写手机认证码"),
    USER_GOOGLE_AUTH_CODE_FILL("200001028", "必须填写谷歌认证码"),
    USER_PWD_ERROR_NOT_COUNT("200001029", "密码错误不判断次数"),
    USER_ACTIVE_EMAIL_REFUSE_SEND("200001030", "发送过于频繁,请{0}分钟后重试"),
    USER_LINK_EXPIRED("200001031", "链接已经过期"),
    USER_INVALID_LINK_CODE("200001032", "无效的链接码"),
    USER_MOBILE_BIND("200001033", "已经绑定了手机"),
    USER_GOOGLE_NOT_BIND("200001034", "2fa未绑定"),
    USER_GOOGLE_CODE_NOT_REUSE("200001035", "请等待新的验证码生成"),
    USER_LOGIN_FREQUENTLY("200001036", "账号频繁登录，请两小时后再试"),
    USER_MOBILE_VERIFY_NO("200001037", "手机验证未开启"),
    USER_GOOGLE_VERIFY_NO("200001038", "2fa验证未开启"),
    USER_WITHDRAW_WHITE_CLOSE("200001039", "白名单已经关闭"),
    USER_WITHDRAW_WHITE_OPEN("200001040", "白名单已经开启"),
    USER_FAILED_TIME_LIMT("200001041", "请稍后再重试"),
    USER_PWD_ERROR_INFO("200001042", "邮箱或密码错误"),
    USER_IP_CHANGE_EMAIL_CONFIRM("200001043", "用户IP变动需要邮寄确认"),
    USER_IP_CHANGE_EMAIL_CONFIRM_ERROR("200001044", "用户IP变动确认失败"),
    USER_RESET_PSW_CODE_EXPIRED("200001045", "APP重置密码验证码失效"),
    USER_INVALID_RESET_PSW_CODE("200001046", "APP重置密码验证码无效"),
    USER_SERCURITY_NOT_BIND("200001047", "请绑定谷歌或者短信验证"),
    USER_EMAIL_NOT_CORRECT("200001048", "邮箱格式不正确"),
    USER_REGISTER_IP_EXCEED("200001049", "您的IP注册次数超过限制，请改天再试."),
    USER_MOBILE_AUTH_TIME_LIMITE("200001050", "用户手机认证超过最大限制次数，请稍后重新获取"),
    USER_UPDATE_PWD_ERROR("200001051", "修改密码失败，原密码不正确"),
    USER_UPDATE_PASSWORD_ERROR("200001052", "密码输错超过限定次数，请登录后再修改密码"),
    USER_ACCOUNT_NOT_EXIST("200001053", "账户不存在"),
    USER_ILLEGAL_PARAMETER("200001054", "参数不合法"),
    USER_SPECIAL_CHARACTERS_ERROR("200001055", "请输入非特殊字符"),
    USER_FAIL_TO_REGISTER("200001056", "注册失败，请您联系客服"),
    USER_DISABLED_LOGIN("200001057", "你的账户已被限制登录"),
    // 200002xxx 用户设备指纹相关错误码
    USER_DEVICE_ERROR("200002001", "设备信息异常"),
    USER_DEVICE_TOO_LONG("200002002", "设备信息异常，超过长度上限"),
    USER_DEVICE_INFO_EMPTY_WEB("200002003", "您当前登录的浏览器存在安全隐患，请使用其他浏览器再次尝试登录。(推荐浏览器：Chrome)"),
    USER_DEVICE_UNAUTHORIZED("200002040", "您正在尝试用新的设备登录币安账号,请在邮件中授权设备。"),

    USER_IN_BLOCKLIST("200003001", "当前账户在禁用名单内，不允许操作"),
    USER_IN_CME_FORBIDDEN("200003002", "当前账户在cme名单内，不允许操作"),
    USER_IN_BLACK_KYC("200003003", "当前账户在禁用kyc列表内，不允许操作"),
    USER_IN_BLACK_IP_AND_NO_KYC("200003004", "当前账户在禁用ip列表内，并且没有kyc，不允许操作"),
    USER_IN_BLACK_IP_AND_KYC("200003005", "当前账户在禁用ip列表内，有kyc，不允许操作"),
    USER_IN_BLACK_DEVICE_RELATION("200003006", "当前账户在禁用关联设备列表内，不允许操作"),

    // for stock token project
    USER_IN_CN_FORBIDDEN("200003500", "当前账户在中国，不允许操作"),
    USER_NOT_ACCEPTED_ST_TERMS("200003501", "Please confirm the user agreement to access the service"),
    USER_DOES_NOT_REACH_KYC_LV2("200003999", "当前账户没达到KYC LV2"),



    USER_KYC_PENDING("200010000", "正在审核中"),
    USER_KYC_UPLOAD_EXCEED_LIMIT_TODAY("200010001", "今日申请身份认证次数超过限制"),
    USER_KYC_PASSED("200010002", "身份认证已经通过"),

    CAPITAL_NO_CHARGE("300001001", "地址没有入金"),
    CAPITAL_MINUTE_TOO_SMALL("300001002", "请一分钟以后再试"),
    CAPITAL_CHARGE_NOT_RESET("300001003", "该币种不能重新获取入金地址"),
    CAPITAL_ADDRESS_TOO_MUCH("300001004", "二十四小时内使用充值地址超过100次。"),

    MSG_NO_MSG("400001001", "消息体不能为空"),
    MSG_NO_TEMPLATE("400001002", "未知的模板类型"),
    MSG_NO_IP("400001003", "用户ip不能为空"),
    MSG_NO_1MIN_LIMIT("400002001", "1分钟内发送验证码，被阻止"),
    MSG_NO_MAX_LIMIT("400002002", "验证码已超过{0}次，请明日再操作！"),
    MSG_NO_VERIFY_INVALID("400002003", "无效的验证码！"),

    COMMON_INCOMPLETE_INFO("500000001", "信息填写不完整"),
    COMMON_STATE_EXPIRE("500000002", "当前记录已过期"),
    COMMON_TRY_AGAIN_LATER("500000003", "请{0}分钟后重试"),

    // For AuthCenter
    AC_ACCOUNT_NOT_EXIST("600000001", "账户不存在"),
    AC_EMAIL_VERIFY_FAILED("600000002", "请输入正确的邮箱"),
    AC_GEETEST_CLOSED("600000003", "极验证已关闭"),
    AC_VALIDATE_FAILED_REFRESH_AND_RETRY("600000004", "验证失败,请刷新后重试"),
    AC_DRAG_AND_DROP("600000005", "请按提示拖动滑块"),
    AC_ALIYUN_VALIDATE_CODE_CLOSED("600000006", "阿里云验证服务已关闭"),
    AC_PLEASE_RELOGIN("600000007", "登录已失效，请重新登录"),
    /**
     * 新标准ErrorCode:
     * <p>
     * 1. {微服务端口号(去除第一位9)}{从001开始递增} 例如，微服务Gateway端口号9015，则Gateway的ErrorCode为 015001, 015002等
     * <p>
     * 2. PNK,Exchange等老系统使用999，998等开头，依次递减
     */
    PNK_ERROR("999001", "PNK系统错误"),
    EXCHANGE_ERROR("998001", "Exchange系统错误"),
    // common 000开头
    OK("000000", "正常"),
    ERROR("000001", "系统异常"),
    ILLEGAL_PARAM("000002", "参数异常"),
    TOO_MANY_REQUESTS("000003", "系统访问量大，请稍后重试"),
    SERVICE_HEAVY_LOAD("000004", "系统负载过大，请稍后重试"),

    // account 9001
    API_KEY_CREATE_FAIL("001001", "创建API KEY失败"),
    API_KEY_UPDATE_FAIL("001002", "更新API KEY失败"),
    API_KEY_DELETE_FAIL("001003", "删除API KEY失败"),
    API_KEY_NOT_FOUND("001004", "API KEY不存在"),
    API_KEY_NEED_CONFIRM("001005", "请在邮件中进行确认"),
    API_KEY_CONFIRM_TIMEOUT("001006", "确认超时"),
    API_KEY_INVALID_CODE("001007", "验证码无效"),
    API_KEY_DISABLED("001008", "API KEY不可用"),
    API_KEY_HAVE_BEEN_VERIFIED("001009", "已经确认过了"),
    API_KEY_NOT_VERIFY("001010", "未确认"),
    API_KEY_MAINTENANCE("001011", "系统维护中"),
    API_KEY_SIGN_ERROR("001012", "签名验证失败"),

    SUB_UER_FUNCTION_NOT_ENABLED("001013", "未开通母子账户功能"),
    SUB_UER_FUNCTION_ALREADY_ENABLED("001014", "请勿重复开通母子账户功能"),
    NOT_SUB_USER("001015", "该账户不是子账户"),
    SUB_USER_ALREADY_BOUND("001016", "该子账户已被绑定"),
    TWO_USER_ID_NOT_BOUND("001017", "两个账户不是母子账户关系"),
    SUB_USER_NOT_ENABLED("001018", "子账户已被母账户禁用"),
    API_KEY_UPDATE_IP_APPOINT("001019", "api提现权限必须指定ip"),
    API_KEY_UPDATE_IP_ILLEGEL("001020", "非法IP地址"),
    API_KEY_WITHDRAW_EAMIL_ENABEL("001021", "前端提示——请查看您的邮件确认开启API提现功能。"),
    COUNTRY_NOT_SUPPORT("001022", "根据您当前的IP地址，Binance无法为您所在的国家/地区提供服务。"),
    AC_RESET_EMAIL_EXPIRED("001023", "邮件已过期"),
    AC_RESET_FACE_TOKEN_FAIL("001024", "获取Web端人脸识别Token失败"),
    AC_RESET_FACE_STATUS_UNDO("001025", "当前状态不能做人脸识别"),
    AC_RESET_FACE_HAND_PHOTO_MISS("001026", "手持照片缺失"),
    AC_RESET_FACE_DAILY_TIMES("001027", "人脸识别次数超出单日限制次数"),
    AC_RESET_FACE_MINUTE_EMAIL("001028", "人脸识别邮件发送太频繁"),
    AC_RESET_FACE_SDK_QR_TIMEOUT("001029", "二维码过期"),
    AC_RESET_RECORD_MISS("001030", "重置记录获取失败"),
    SUB_USER_MAX_TOTAL("001031", "最多创建{0}个子账户"),
    BLACKLISTUSER_NOT_ENABLED("001032", "该账户是黑名单账户"),
    COUNTRY_RESTRICTED("001033", "您尝试访问的服务目前在您所在的地区无法使用。"),

    //更换邮箱状态
    USER_EMAIL_CHANGE_NEED_KYC("001034","需要进行KYC验证"),
    USER_EMAIL_CHANGE_NEED_FACE("001035","需要进行face验证"),
    USER_EMAIL_CHANGE_FACE_TIMEOUT("001036","调用face超时"),
    USER_EMAIL_CHANGE_ALREADY_REG("001037","该邮箱已经注册过"),

    API_KEY_UPDATE_EAMIL_ENABLE("001038", "前端提示——请查看您的邮件确认修改APIKEY操作"),
    USER_EMAIL_SUCCESS_COUNT("001039","更换邮箱超过更换次数"),

    // gateway 9015
    GW_NEWPWD_EQUAL_OLDPWD("015001", "新旧密码不能相同"),
    GW_TOO_MANY_REQUESTS("015002", "请求过于频繁，请稍后重试"),

    // binance-mgs 9024
    BNB_FEE_CLOSE("024001", "BNB燃烧开关未打开，无法设置gas"),
    SYSTEM_MAINTENANCE("024002", "系统维护中"),
    API_MAINTENANCE("024003", "系统维护中"),

    // MBX 9027 Don't add mbx error code here, add it in mbx-service
    ORDER_TRADE_SUCCESS("027101", "订单已成交"),
    ORDER_TRADE_REFUSE("027102", "订单已拒绝"),
    ORDER_TRADE_OVER_TIME("027103", "订单已过期"),
    ORDER_TRADE_CANCEL("027104", "订单已撤销"),
    ORDER_MARKET_CLOSED("027005", "休市中"),
    ORDER_TRADE_CANCEL_SUCCESS("027006", "撤单成功"),
    ORDER_INSUFFICIENT_BALANCE("027007", "下单失败：余额或持仓不足"),
    ORDER_TOO_MANY_NEW_ORDERS("027008", "下单失败：下单过于频繁"),
    ORDER_LOT_SIZE("027009", "小于最小下单数量{3}。"),
    ORDER_PRICE_FILTER("027010", "操作失败:下单价格超出正常范围"),
    ORDER_MIN_NOTIONAL("027011", "下单总金额须大于{0}。"),
    ORDER_T_PLUS_SELL("027012", "下单失败：T+N卖出限制"),
    ORDER_MAX_POSITION("027013", "下单失败：超过最大持仓上限"),
    ORDER_THIS_ACTION_DISABLED("027014", "下单失败：交易禁用"),
    ORDER_MIN_MAX("027015", "下单失败：委托价格超过涨跌幅限制"),
    ORDER_PRICE_X_QTY("027016", "下单数量或价格超过范围"),
    ORDER_QTY_UNDER_MINIMUM_QTY("027017", "下单失败：小于最小下单量"),
    ORDER_QTY_OVER_MAXIMUM_QTY("027018", "超出最大下单数量{1}。"),
    ORDER_PRICE_OVER_MAXIMUM_PRICE("027019", "超出最大下单价格{2}。"),
    // ORDER_PRICE_UNDER_MINIMUM_PRICE("",""),
    ORDER_MARKET_IS_CLOSED("027020", "下单失败：非交易时间不能下单"),
    ORDER_LESS_ZERO("027021", "超出最大下单数量{1}。"),
    ORDER_INVALID_QUANTITY("027022", "请输入有效的数字。"),
    ORDER_MAX_NUM_ALGO_ORDERS("027023", "每个市场最多只能创建5个止盈止损单"),
    ORDER_MAX_PRICE("027024", "价格不能高于{0}"),
    ORDER_MIN_PRICE("027025", "价格不能低于{0}"),
    ORDER_TICK_SIZE_LIMIT("027026", "下单数量必须是{0}的整数倍"),
    PRODUCT_PRICE_EMPTY("027027", "价格必须大于0。"),
    KEY_DELETE_READ_KEY_FAILED("027028","删除读取权限失败"),
    API_ENABLEAPIWITHDRAW_NOT_EXIST("027029","无效验证码"),
    API_NOT_BEEN_VERIFY("027030","Api未被邮件确认"),
    API_ENABLEAPIWITHDRAW_ENABLED("027031","api启用了"),
    API_ENABLEAPIWITHDRAW_DISABLE("027032"," API已经被禁用了"),
    API_ENABLEAPIWITHDRAW_TIME_FAIL("027033","验证码失效"),
    API_ENABLEAPIWITHDRAW_SUCCESS("027034", "您的API提现功能已开启，可以到API页面查看状态。"),
    API_HAVE_BEEN_VERIFY("027035","Api已经被邮件确认"),
    KEY_API_KEY_NOT_EXIST("027036","API不存在"),
    PLACE_ORDER_ERROR("027037","{0}"),

    // ASSET-SERVICE 9032
    ASSET_DRIBBLET_CONVERT_SWITCH_OFF("032001", "小额资产转换开关dribblet_convert_switch未打开"),
    ASSET_ASSET_NOT_ENOUGH("032002", "该用户可用余额不够"),
    ASSET_USER_HAVE_NO_ASSET("032003", "该用户没有此币种"),
    USER_OUT_OF_TRANSFER_FLOAT("032004", "{0} 剩余资产的估值已超过0.001BTC，请重新选择"),
    USER_ASSET_AMOUNT_IS_TOO_LOW("032005", "{0} 剩余资产的估值过低，请重新选择"),
    USER_CAN_NOT_REQUEST_IN_24_HOURS("032006", "24小时内只能转化一次"),
    AMOUNT_OVER_ZERO("032007", "数量必须大于零"),
    ASSET_WITHDRAW_WITHDRAWING_NOT_ENOUGH("032008", "可退回资产数量不足"),
    PRODUCT_NOT_EXIST("032009", "产品不存在"),



    // NOTIFICATON 9003
    AP_BREAK_SIDE_DOWN_PRICE_MUST_UNDER_NOW("003001", "向下突破时设置的价格必须小于当前交易价格且不能低于其1%"),
    AP_BREAK_SIDE_UP_PRICE_MUST_OVER_NOW("003002", "向上突破时设置的价格必须大于当前交易价格且不能高于其100倍"),
    AP_BREAK_MUST_NOT_NE_NOW_PRICE("003003", "设置的价格不能等于当前交易价格"),

    BROKER_SUB_UER_FUNCTION_ALREADY_ENABLED("003011", "请勿重复开通broker母子账户功能"),
    BROKER_SUB_UER_FUNCTION_NOT_ENABLED("003012", "未开通broker母子账户功能"),
    BROKER_SUB_USER_MAX_TOTAL("001033", "Broker最多创建{0}个子账户"),
    ASSET_SUB_UER_IS_ALREADY_ENABLED("003014", "请勿重复开通资产子账户功能"),
    ;

    private String code;

    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private GeneralCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GeneralCode findByCode(String code) {
        GeneralCode result = null;
        for (GeneralCode temp : GeneralCode.values()) {
            if (StringUtils.equals(temp.getCode(), code)) {
                result = temp;
                break;
            }
        }
        return result;
    }

}
