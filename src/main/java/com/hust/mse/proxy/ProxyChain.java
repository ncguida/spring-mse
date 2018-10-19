package com.hust.mse.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理链
 *
 * @author ncguida
 * @date 2018/10/14
 */
public class ProxyChain {

    private final Class<?> targetClass;

    private final Object targetObject;

    private final Method targetMethod;

    private final Object[] params;

    private final MethodProxy methodProxy;

    private List<Proxy> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] params,
                      MethodProxy methodProxy, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.params = params;
        this.methodProxy = methodProxy;
        this.proxyList = proxyList;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {

            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(targetObject, params);
        }
        return methodResult;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getParams() {
        return params;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public List<Proxy> getProxyList() {
        return proxyList;
    }
}
