package com.hust.mse.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stream
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class StreamUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream is) {

        StringBuffer buffer = new StringBuffer();

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferedReader.readLine();

            while (line != null) {
                buffer.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException ioe) {
            LOGGER.error("get string  from inputstream failed", ioe);
            throw new RuntimeException(ioe);
        }
        return buffer.toString();
    }

}
