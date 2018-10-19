package com.hust.mse.helper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.hust.mse.annotation.NameInject;
import com.hust.mse.annotation.Primary;
import com.hust.mse.annotation.TypeInject;
import com.hust.mse.utils.ReflectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/14
 */
public class IocHelper {


    static {
        //解析依赖注入
        for (Entry<String, Object> entry : BeanHelper.getName2BeanMap().entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();
            Class clazz = BeanHelper.getName2ClassMap().get(beanName);
            parseInject(beanName, clazz, bean);
        }
    }


    private static void parseInject(String beanName, Class clazz, Object bean) {
        Class clz = clazz;
        while (!clz.equals(Object.class)) {
            doParseInject(beanName, clz, bean);
            clz = clz.getSuperclass();
        }
    }

    private static void doParseInject(String beanName, Class clz, Object bean) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(NameInject.class)) {
                parseNameInject(beanName, clz, bean, field);
            }

            if (field.isAnnotationPresent(TypeInject.class)) {
                parseTypeInject(beanName, clz, bean, field);
            }
        }
    }

    private static void parseTypeInject(String beanName, Class clz, Object bean, Field field) {
        Class<?> fieldClz = field.getType();
        Set<Object> set = BeanHelper.getType2BeanSetMap().get(fieldClz);

        if (CollectionUtils.isEmpty(set)) {
            throw new RuntimeException("class bean :" + fieldClz.getName() + "not found ");
        }

        if (set.size() == 1) {
            ReflectionUtil.setField(field, bean, set.iterator().next());
            return;
        }
        Set<Object> candidateSet = new HashSet<>();
        for (Object obj : set) {
            if (obj.getClass().isAnnotationPresent(Primary.class)) {
                candidateSet.add(obj);
            }
        }

        if (CollectionUtils.isEmpty(candidateSet) || candidateSet.size() != 1) {
            throw new RuntimeException("no class bean or more than 1 class bean ");
        }

        ReflectionUtil.setField(field, bean, candidateSet.iterator().next());
    }

    private static void parseNameInject(String beanName, Class clz, Object bean, Field field) {
        NameInject annotation = field.getAnnotation(NameInject.class);

        String injectBeanName = annotation.value();
        if (StringUtils.isBlank(injectBeanName)) {
            injectBeanName = field.getName();
        }

        Object injectBean = BeanHelper.getName2BeanMap().get(injectBeanName);
        if (injectBean == null) {
            throw new RuntimeException("bean:" + injectBeanName + " not found");
        }

        ReflectionUtil.setField(field, bean, injectBean);
    }

}
