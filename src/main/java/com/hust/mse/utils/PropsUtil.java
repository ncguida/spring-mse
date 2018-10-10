package com.hust.mse.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性工具类
 *
 * @author ncguida
 * @date 2018/10/10
 */
public final class PropsUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String filename) {
        Properties props = null;
        InputStream in = null;

        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

            if (in == null) {
                throw new FileNotFoundException("file:" + filename + " not  found");
            }
            props = new Properties();
            props.load(in);
        } catch (IOException ioe) {
            LOGGER.error("load properties failed,file:" + filename);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("close inputstream failed");
                }
            }
        }
        return props;
    }

    public static String getString(Properties props, String key, String defaultValue) {
        if (props == null) {
            return null;
        }
        if (props.containsKey(key)) {
            return props.getProperty(key);
        }

        return defaultValue;
    }

    public static String getString(Properties props, String key) {
        return getString(props, key, null);
    }

    public static int getInt(Properties props, String key, int defaultValue) {
        return CastUtil.castInt(getString(props, key), defaultValue);
    }

    public static int getInt(Properties props, String key) {
        return CastUtil.castInt(getString(props, key));
    }

    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        return CastUtil.castBoolean(getString(props, key), defaultValue);
    }

    public static boolean getBoolean(Properties props, String key) {
        return CastUtil.castBoolean(getString(props, key));
    }

}
