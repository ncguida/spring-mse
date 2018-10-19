package com.hust.mse.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理管理器
 *
 * @author ncguida
 * @date 2018/10/14
 */
public class ProxyManager {

    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {

        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return new ProxyChain(targetClass, obj, method, args, proxy, proxyList).doProxyChain();
            }
        });
    }

}
