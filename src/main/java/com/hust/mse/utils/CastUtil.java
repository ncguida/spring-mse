package com.hust.mse.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 转换类
 *
 * @author ncguida
 * @date 2018/10/10
 */
public final class CastUtil {

    public static String castString(Object value, String defaultValue) {
        return value == null ? defaultValue : String.valueOf(value);
    }

    public static String castString(Object value) {
        return castString(value, null);
    }

    public static double castDouble(Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String s = castString(value);

        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static double castDouble(Object value) {
        return castDouble(value, 0);
    }

    public static long castLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String s = castString(value);
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }

        try {
            return Long.parseLong(s);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Long castLong(Object value) {
        return castLong(value, 0L);
    }

    public static int castInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String s = castString(value);
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static int castInt(Object value) {
        return castInt(value, 0);
    }

    public static boolean castBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(castString(value));
    }

    public static boolean castBoolean(Object value) {
        return castBoolean(value, false);
    }

}
