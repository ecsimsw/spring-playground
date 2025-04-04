package com.ecsimsw.common.support.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtils {

    private static final String ALGORITHM = "AES";

    public static String encrypt(String secret, String data){
        try {
            var cipher = Cipher.getInstance(ALGORITHM);
            var secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            var encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Encryption failed", e);
        }
    }

    public static String decrypt(String secret, String encryptedData) {
        try {
            var cipher = Cipher.getInstance(ALGORITHM);
            var secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            var decodedBytes = Base64.getDecoder().decode(encryptedData);
            var decryptedData = cipher.doFinal(decodedBytes);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Decryption failed", e);
        }
    }
}
