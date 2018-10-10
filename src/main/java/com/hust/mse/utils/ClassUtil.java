package com.hust.mse.utils;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 *
 * @author ncguida
 * @date 2018/10/10
 */
public class ClassUtil {

    private  static  final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    public static  ClassLoader getClasLoader(){
        return  Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClasLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class fail", e);
            throw new RuntimeException(e);
        }
        return cls;
    }


    public  static Set<Class<?>>  getClassSet(String packageName){


        return  null;
    }



}
