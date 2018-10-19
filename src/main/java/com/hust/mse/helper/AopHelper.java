package com.hust.mse.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.hust.mse.annotation.Aspect;
import com.hust.mse.proxy.AspectProxy;
import com.hust.mse.proxy.Proxy;
import com.hust.mse.proxy.ProxyManager;
import com.hust.mse.utils.ReflectionUtil;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/14
 */
public class AopHelper {

    static {
        try {
            Map<Class<?>, Set<Class<?>>> aspect2TargetMap = createAspect2TargetMap();

            Map<Class<?>, List<Proxy>> target2ProxyMap = createTarget2ProxyMap(aspect2TargetMap);

            for (Entry<Class<?>,List<Proxy>> entry:target2ProxyMap.entrySet()){
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxies = entry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxies);
                BeanHelper.setBean(targetClass,proxy);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Class<? extends Annotation> annotation = aspect.value();
        return ClassHelper.getClassesByAnnotaion(annotation);
    }

    public static Map<Class<?>, Set<Class<?>>> createAspect2TargetMap() {
        Map<Class<?>, Set<Class<?>>> aspect2TargetMap = new HashMap<>();
        Set<Class<?>> aspectClasses = ClassHelper.getClassesBySuper(AspectProxy.class);

        for (Class<?> aspectClass : aspectClasses) {

            if (aspectClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspectAnnotation = aspectClass.getAnnotation(Aspect.class);
                Class<? extends Annotation> targetAnnotation = aspectAnnotation.value();
                Set<Class<?>> targetClasses = ClassHelper.getClassesByAnnotaion(targetAnnotation);
                aspect2TargetMap.put(aspectClass, targetClasses);
            }
        }

        return aspect2TargetMap;
    }

    public static Map<Class<?>,List<Proxy>>  createTarget2ProxyMap(Map<Class<?>, Set<Class<?>>>  aspect2TargetMap){

        Map<Class<?>,List<Proxy>>  target2ProxyMap =new HashMap<>();

        for (Entry<Class<?>,Set<Class<?>>> entry: aspect2TargetMap.entrySet()){

            Class<?> aspectClass = entry.getKey();

            Proxy proxy = (Proxy)ReflectionUtil.newInstance(aspectClass);

            for (Class<?>  targetClass:entry.getValue()){
                if (target2ProxyMap.containsKey(targetClass)){
                    target2ProxyMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxies = new ArrayList<>();
                    proxies.add(proxy);
                    target2ProxyMap.put(targetClass,proxies);
                }
            }
        }
        return  target2ProxyMap;
    }




}
