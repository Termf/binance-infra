package com.binance.platform.mbx.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;

public class FormatUtils {
    private static ThreadLocal<Format> AVG_PRICE_NUMERIC_FORMATTER = new ThreadLocal<Format>() {
        @Override
        protected Format initialValue() {
            return new DecimalFormat("###.########");
        }
    };

    private static ThreadLocal<Format> PRICE_NUMERIC_FORMATTER = new ThreadLocal<Format>() {
        @Override
        protected Format initialValue() {
            return new DecimalFormat("###.########");
        }
    };

    private static ThreadLocal<Format> MONEY_NUMERIC_FORMATTER = new ThreadLocal<Format>() {
        @Override
        protected Format initialValue() {
            return new DecimalFormat("###.########");
        }
    };

    private static ThreadLocal<Format> ASSET_NUMERIC_FORMATTER = new ThreadLocal<Format>() {
        @Override
        protected Format initialValue() {
            DecimalFormat df = new DecimalFormat("###.########");
            df.setRoundingMode(RoundingMode.DOWN);
            return df;
        }
    };

    public static Format getAvgPriceNumericFormatter() {
        return AVG_PRICE_NUMERIC_FORMATTER.get();
    }

    public static Format getPriceNumericFormatter() {
        return PRICE_NUMERIC_FORMATTER.get();
    }

    public static Format getMoneyNumericFormatter() {
        return MONEY_NUMERIC_FORMATTER.get();
    }

    public static Format getAssetNumericFormatter() {
        return ASSET_NUMERIC_FORMATTER.get();
    }

}