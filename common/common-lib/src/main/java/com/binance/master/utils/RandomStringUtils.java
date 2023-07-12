package com.binance.master.utils;

import java.security.SecureRandom;

public class RandomStringUtils {
    private static final String BASE_RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMBER_RANDOM_CHARS = "0123456789";
    private static SecureRandom secureRandom = new SecureRandom();

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int size = BASE_RANDOM_CHARS.length();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(BASE_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    public static String getNumberRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int size = NUMBER_RANDOM_CHARS.length();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(NUMBER_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    public static String getPasswordSalt() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()<>,.:;'";
        int length = 40;
        int size = base.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
