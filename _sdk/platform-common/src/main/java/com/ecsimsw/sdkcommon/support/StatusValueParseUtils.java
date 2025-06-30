package com.ecsimsw.sdkcommon.support;

public class StatusValueParseUtils {

    public static int mapRange(int value, int fromMin, int fromMax, int toMin, int toMax) {
        value = Math.max(fromMin, Math.min(fromMax, value));
        if (fromMax == fromMin) {
            throw new IllegalArgumentException("fromMax, fromMin can't be same");
        }
        double ratio = (double)(value - fromMin) / (fromMax - fromMin);
        double mapped = ratio * (toMax - toMin) + toMin;
        return (int)Math.round(mapped);
    }
}
