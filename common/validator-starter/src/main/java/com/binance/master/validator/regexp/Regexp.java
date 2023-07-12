package com.binance.master.validator.regexp;

public final class Regexp {

    public final static String LOGIN_EMAIL =
            "^[a-zA-Z0-9_-|\\W]+(\\.[a-zA-Z0-9_-|\\W]+)*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // "\", "~","`","!","！","$","￥","…","#","%","@", "^","&","*","(", ")", "+", ".", "[",
    // "]","【","】", "?", "{", "}", "|","<",">"
    public final static String PHISHING_CODE_IGNORE = "^[^\\\\~`!！\\$￥…#%@\\^&\\(\\)\\+\\.\\[\\]【 】?\\{\\}\\|<>]+$";

}
