package com.hust.mse.bean;

import java.lang.reflect.Method;

/**
 * 封装action信息
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class Handler {

    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * Action method
     */
    private Method  actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Handler() {
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
