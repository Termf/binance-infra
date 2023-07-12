package com.binance.platform.monitor.configuration.cloudwatch.internal;

/**
 * Utilities for math.
 *
 * @author Dawid Kublik
 */
public final class MathUtils {

    private MathUtils() {}

    // Simplified {@link com.google.common.math.IntMath#divide(int, int, java.math.RoundingMode)}.
    public static int divideWithCeilingRoundingMode(int p, int q) {
        if (q == 0) {
            throw new ArithmeticException("/ by zero"); // for GWT
        }
        int div = p / q;
        int rem = p - q * div; // equal to p % q

        if (rem == 0) {
            return div;
        }

        /*
         * Normal Java division rounds towards 0, consistently with RoundingMode.DOWN. We just have to
         * deal with the cases where rounding towards 0 is wrong, which typically depends on the sign of
         * p / q.
         *
         * signum is 1 if p and q are both nonnegative or both negative, and -1 otherwise.
         */
        int signum = 1 | ((p ^ q) >> (Integer.SIZE - 1));
        boolean increment = signum > 0;
        return increment ? div + signum : div;
    }

}
