package com.ecsimsw.device.support;

public class ConverterUtils {

    public static boolean isStringInteger(Object obj) {
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Byte) {
            return true;
        }
        if (obj instanceof String) {
            try {
                Integer.parseInt((String) obj);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
