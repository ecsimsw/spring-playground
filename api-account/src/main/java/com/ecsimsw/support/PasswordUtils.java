package com.ecsimsw.support;

import java.security.SecureRandom;

public class PasswordUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateRandom(int length) {
        var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
