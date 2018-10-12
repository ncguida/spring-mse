package com.hust.mse.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射Util
 *
 * @author ncguida
 * @date 2018/10/12
 */
public class ReflectionUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object newInstance(Class<?> clazz) {

        try {
            Object instance = clazz.newInstance();
            return instance;
        } catch (Exception e) {
            LOGGER.error("new instance failed", e);
            throw new RuntimeException(e);
        }

    }

    public static void invokeMethod(Method method, Object object, Object... args) {

        try {
            method.invoke(object, args);
        } catch (Exception e) {
            LOGGER.error("invoke method failed", e);
            throw new RuntimeException(e);
        }
    }

    public static void setField(Field field, Object object, Object arg) {

        try {
            field.setAccessible(true);
            field.set(object, arg);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failed", e);
            throw new RuntimeException(e);
        }
    }

}
