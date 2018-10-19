package com.hust.mse.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.hust.mse.annotation.Controller;
import com.hust.mse.annotation.Service;
import com.hust.mse.utils.ClassUtil;

/**
 * 类操作助手类
 *
 * @author ncguida
 * @date 2018/10/12
 */
public class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {

        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }

    public static Set<Class<?>> getClassesBySuper(Class<?> superClass) {

        Set<Class<?>> classSet = new HashSet<>();

        for (Class clz : CLASS_SET) {

            if (clz.isAssignableFrom(superClass) && !clz.equals(superClass)) {
                classSet.add(clz);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getClassesByAnnotaion(Class<? extends Annotation> annotationClz) {
        Set<Class<?>> classSet = new HashSet<>();

        for (Class clz : CLASS_SET) {
            if (clz.isAnnotationPresent(annotationClz)) {
                classSet.add(clz);
            }

        }

        return classSet;
    }

}
