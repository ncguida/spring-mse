package com.hust.mse.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class CodeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);

    public static String encodeUrl(String source) {

        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("encode url failed", e);
            throw new RuntimeException(e);
        }
    }

    public static String decodeUrl(String source) {

        try {
            return URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode url failed", e);
            throw new RuntimeException(e);
        }
    }

}
