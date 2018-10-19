package com.hust.mse.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.hust.mse.annotation.Controller;
import com.hust.mse.annotation.NameInject;
import com.hust.mse.annotation.Primary;
import com.hust.mse.annotation.Service;
import com.hust.mse.annotation.TypeInject;
import com.hust.mse.utils.ReflectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ncguida
 * @date 2018/10/12
 */
public class BeanHelper {

    /**
     * name --> Bean map
     */
    private static Map<String, Object> name2BeanMap = new ConcurrentHashMap<>();

    /**
     * class --> Set<Bean>  map
     */
    private static Map<Class, Set<Object>> type2BeanSetMap = new ConcurrentHashMap<>();

    /**
     * name --> class  map
     */
    private static Map<String, Class> name2ClassMap = new ConcurrentSkipListMap<>();

    private static Object mutex = new Object();

    static {

        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();

        for (Class clazz : beanClassSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                doLoadServiceClass(clazz);
            }

            if (clazz.isAnnotationPresent(Controller.class)) {
                doLoadControllerClass(clazz);
            }
        }
    }

    public static  Map<String,Object> getName2BeanMap(){
        return name2BeanMap;
    }

    public static Map<Class, Set<Object>> getType2BeanSetMap() {
        return type2BeanSetMap;
    }

    public static Map<String, Class> getName2ClassMap() {
        return name2ClassMap;
    }

    public static Object getByName(String beanName) {
        return name2BeanMap.get(beanName);
    }

    public static void  setBean(Class<?> clz,Object obj){

        HashSet<String> candidates = new HashSet<>();
        for (Entry<String,Class> entry:name2ClassMap.entrySet()){
            Class clazz = entry.getValue();
            if (clazz.equals(clz)){
                candidates.add(entry.getKey());
            }
        }

        if (CollectionUtils.isEmpty(candidates)){
            return;
        }

        for (String beanName : candidates) {
            addBean(beanName, clz, obj);
        }
    }

    public static Object getByType(Class clz) {
        Set<Object> set = type2BeanSetMap.get(clz);

        if (CollectionUtils.isEmpty(set)) {
            throw new RuntimeException("no class or more than 1 class bean ,classname:" + clz.getName());
        }

        Set<Object> candidateSet = new HashSet<>();
        for (Object obj : set) {
            if (obj.getClass().isAnnotationPresent(Primary.class)) {
                candidateSet.add(obj);
            }
        }
        if (CollectionUtils.isEmpty(candidateSet) || candidateSet.size() != 1) {
            throw new RuntimeException("no class or more than 1 class bean ,classname:" + clz.getName());
        }

        return set.iterator().next();
    }

    public static List<Object>  getObjsByType(Class clz){
        Set<Object> objects = type2BeanSetMap.get(clz);
        return new ArrayList<>(objects);
    }



    private static void doLoadControllerClass(Class clazz) {
        Controller annotation = (Controller)clazz.getAnnotation(Controller.class);
        String beanName = annotation.value();

        if (StringUtils.isBlank(beanName)) {
            beanName = parseBeanName(clazz);
        }
        Object o = ReflectionUtil.newInstance(clazz);

        addBean(beanName, clazz, o);
    }

    private static void doLoadServiceClass(Class clazz) {

        Service annotation = (Service)clazz.getAnnotation(Service.class);
        String beanName = annotation.value();

        if (StringUtils.isBlank(beanName)) {
            beanName = parseBeanName(clazz);
        }
        Object o = ReflectionUtil.newInstance(clazz);

        addBean(beanName, clazz, o);
    }

    private static void addBean(String beanName, Class clazz, Object o) {

        synchronized (mutex) {
            name2BeanMap.put(beanName, o);
            name2ClassMap.put(beanName, clazz);

            Set<Object> beanSet = type2BeanSetMap.get(clazz);
            if (CollectionUtils.isEmpty(beanSet)) {
                beanSet = new HashSet<>();
            }
            beanSet.add(o);

            type2BeanSetMap.put(clazz, beanSet);
        }

    }

    private static String parseBeanName(Class clazz) {
        String className = clazz.getSimpleName();
        return StringUtils.uncapitalize(className);
    }

    public static void main(String[] args) {
        System.out.println(1);
    }

}
