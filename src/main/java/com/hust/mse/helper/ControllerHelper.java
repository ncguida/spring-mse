package com.hust.mse.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hust.mse.annotation.Action;
import com.hust.mse.bean.Handler;
import com.hust.mse.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 控制器 帮助类
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class ControllerHelper {

    private final static Map<Request, Handler> BEAN_MAP = new HashMap<>();

    private final static String PATTERN = "\\w+:\\w*";

    static {

        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtils.isNotEmpty(controllerClassSet)) {

            for (Class<?> controllerClz : controllerClassSet) {
                parseControllerAction(controllerClz);

            }
        }
    }

    public static Handler getHandler(String requestPath, String requestMethod) {
        Request request = new Request(requestPath, requestMethod);
        return BEAN_MAP.get(request);
    }

    private static void parseControllerAction(Class<?> controllerClz) {

        Class clz = controllerClz;

        while (!clz.equals(Object.class)) {

            Method[] methods = clz.getDeclaredMethods();

            if (ArrayUtils.isEmpty(methods)) {
                return;
            }

            for (Method method : methods) {
                if (method.isAnnotationPresent(Action.class)) {
                    parseControllerActionMethod(controllerClz, method);
                }
            }
        }
    }

    private static void parseControllerActionMethod(Class<?> controllerClz, Method method) {
        Action annotation = method.getAnnotation(Action.class);
        String mapping = annotation.value();

        if (mapping.matches(PATTERN)) {

            String[] split = mapping.split(":");
            if (ArrayUtils.isNotEmpty(split) && split.length == 2) {
                Request request = new Request(split[0], split[1]);

                Handler handler = new Handler(controllerClz, method);
                BEAN_MAP.put(request, handler);
            }
        }
    }

}
