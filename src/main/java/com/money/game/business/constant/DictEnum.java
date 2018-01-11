package com.money.game.business.constant;

/**
 * conan
 * 2017/10/10 16:33
 **/
public enum DictEnum {


    USER_TYPE_01("01", "普通用户"),
    USER_TYPE_02("02", "系统用户"),

    USER_STATUS_NORMAL("01", "正常"),
    USER_STATUS_FORBIDDER("02", "冻结"),

    ACCOUNT_TYPE_01("01", "基本账户"),
    ACCOUNT_TYPE_05("05", "平台账户"),

    TRADE_TYPE_01("01", "注册赠送"),
    TRADE_TYPE_02("02", "充值"),
    TRADE_TYPE_03("03", "提现"),
    TRADE_TYPE_04("04", "投注"),
    TRADE_TYPE_05("05", "盈利"),

    PAY_STATUS_01("01", "已创建"),
    PAY_STATUS_02("02", "待审核"),
    PAY_STATUS_03("03", "已完成"),

    PAY_AUDIT_STATUS_01("01","未审核"),
    PAY_AUDIT_STATUS_02("02","审核通过"),
    PAY_AUDIT_STATUS_03("03","审核拒绝"),

    PAY_DIRECTION_01("01", "加"),
    PAY_DIRECTION_02("02", "减"),

    MARKEY_PERIOD_1MIN("1min", "一分钟"),
    MARKEY_PERIOD_5MIN("5min", "五分钟"),
    MARKEY_PERIOD_30MIN("30min", "三十分钟"),
    MARKEY_PERIOD_60MIN("60min", "六十分钟"),
    MARKEY_PERIOD_1DAY("1day", "一天"),
    MARKEY_PERIOD_1WEEK("1week", "一周"),
    MARKEY_PERIOD_1MON("1mon", "一月"),
    MARKEY_PERIOD_1YEAR("1year", "一年"),

    MARKEY_SYMBOL_BTCUSDT("btcusdt", "btc市场"),

    DIGITAL_CASH_NAME("大盘猜涨跌", "数字货币名称"),

    GAME_TYPE_01("01", "球赛"),
    GAME_TYPE_02("02", "猜涨跌"),
    GAME_STATUS_01("01", "未开始"),
    GAME_STATUS_02("02", "进行中"),
    GAME_STATUS_03("03", "已结束"),
    GAME_STATUS_04("04","状态异常"),

    GAME_PROPERTIES_WIN("win", "胜"),
    GAME_PROPERTIES_LOSE("lose", "负"),
    GAME_PROPERTIES_DRAW("draw", "平"),
    GAME_PROPERTIES_EXEC("exec","异常"),


    ORDER_STATUS_01("01", "已投注"),
    ORDER_STATUS_02("02", "已完成"),

    PAY_NO_PREFIX("1", "交易流水前缀"),
    ORDER_NO_PREFIX("2", "订单流水前缀"),
    ACCOUNT_NO_PREFIX("AC", "资金账号前缀");


    private String code;

    private String value;

    DictEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
