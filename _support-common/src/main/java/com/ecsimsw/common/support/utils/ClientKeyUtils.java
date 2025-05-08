package com.ecsimsw.common.support.utils;

public class ClientKeyUtils {

    private static final String SECRET = "1234567890123456";

    public static String init() {
        var now = System.currentTimeMillis();
        return AESUtils.encrypt(SECRET, String.valueOf(now));
    }

    public static boolean isValid(String timeKey, int validTimeMs) {
        var decrypted = AESUtils.decrypt(SECRET, timeKey);
        var publishedAt = Long.parseLong(decrypted);
        var now = System.currentTimeMillis();
        return now - publishedAt <= validTimeMs;
    }
}
