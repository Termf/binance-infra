package com.binance.platform.common;

import org.springframework.core.Ordered;

public final class FilterOrder {

    public static final int FILTERORDER_1 = Ordered.HIGHEST_PRECEDENCE;

    public static final int FILTERORDER_2 = Ordered.HIGHEST_PRECEDENCE + 1;

    public static final int FILTERORDER_3 = Ordered.HIGHEST_PRECEDENCE + 2;

    public static final int FILTERORDER_4 = Ordered.HIGHEST_PRECEDENCE + 4;
}
