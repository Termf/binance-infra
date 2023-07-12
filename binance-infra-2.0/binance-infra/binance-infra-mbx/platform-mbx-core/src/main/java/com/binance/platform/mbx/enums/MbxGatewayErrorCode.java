package com.binance.platform.mbx.enums;

import com.binance.master.error.ErrorCode;
import com.binance.master.utils.StringUtils;

/**
 * AuthCenter异常
 *
 */
public enum MbxGatewayErrorCode implements ErrorCode {
    INVOKE_MBX_FAIL("027001", "调用撮合系统失败"),

    BALANCE_BATCH_FAIL("027002", "原子转账失败"),
    BALANCE_INSUFFICIENT("027003", "余额不足"),
    ORDER_AMOUNT_NOT_ENOUGH("027004", "Total value must be more than 10."),

    // import from GeneralCode
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

    private MbxGatewayErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MbxGatewayErrorCode findByCode(String code) {
        MbxGatewayErrorCode result = null;
        for (MbxGatewayErrorCode temp : MbxGatewayErrorCode.values()) {
            if (StringUtils.equals(temp.getCode(), code)) {
                result = temp;
                break;
            }
        }
        return result;
    }

}