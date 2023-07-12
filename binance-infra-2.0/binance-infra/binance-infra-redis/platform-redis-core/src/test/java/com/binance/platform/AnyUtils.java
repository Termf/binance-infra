package com.binance.platform;

import java.util.concurrent.ThreadLocalRandom;

public class AnyUtils {

    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static <T> T anyOf(T... args) {
        return args[random.nextInt(args.length)];
    }

    public static String anyString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 5; i < 15; i++) {
            char c = (char) (random.nextInt(56) + 'A');
            sb.append(c);
        }
        return sb.toString();
    }

    public static int anyInt() {
        return anyInt(100000000);
    }

    public static int anyInt(int max) {
        return random.nextInt(max);
    }

    public static byte anyByte() {
        return (byte)random.nextInt(128);
    }

    public static long anyLong() {
        return anyLong(Long.MAX_VALUE);
    }

    public static long anyLong(long max) {
        return random.nextLong(max);
    }

    public static double anyDouble(double max) {
        return random.nextDouble() * max;
    }

    public static double anyFloat(float max ) {
        return random.nextFloat() * max;
    }

    public static boolean anyBoolean() {
        return random.nextBoolean();
    }

}
