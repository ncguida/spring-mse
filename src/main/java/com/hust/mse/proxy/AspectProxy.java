package com.hust.mse.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/14
 */
public abstract class AspectProxy implements Proxy {

    private final static Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result = null;
        Class<?> targetClass = proxyChain.getTargetClass();

        Object[] params = proxyChain.getParams();

        Method targetMethod = proxyChain.getTargetMethod();

        begin();

        try {
            if (intercept(targetClass, targetMethod, params)) {
                before(targetClass, targetMethod, params);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } finally {
            end();
        }

        return result;
    }

    protected void after(Class<?> targetClass, Method targetMethod, Object[] params) {

    }

    protected void before(Class<?> targetClass, Method targetMethod, Object[] params) throws Throwable {

    }

    protected boolean intercept(Class<?> targetClass, Method targetMethod, Object[] params) throws Throwable {
        return true;
    }

    public void begin() {

    }

    public void end() {

    }

}
