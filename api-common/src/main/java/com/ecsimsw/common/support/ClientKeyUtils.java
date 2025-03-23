package com.ecsimsw.common.support;

public class ClientKeyUtils {

    private static final String SECRET = "1234567890123456";

    public static String init() {
        var now = System.currentTimeMillis();
        return AESUtils.encrypt(SECRET, String.valueOf(now));
    }

    public static void validate(String timeKey) {
        var decrypted = AESUtils.decrypt(SECRET, timeKey);
        var publishedAt = Long.parseLong(decrypted);
        var now = System.currentTimeMillis();
        if(now - publishedAt > 30_000) {
            throw new IllegalArgumentException("Invalid key: " + timeKey);
        }
    }
}
