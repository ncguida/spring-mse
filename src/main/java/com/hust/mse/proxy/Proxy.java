package com.hust.mse.proxy;

/**
 * 代理接口
 *
 * @author ncguida
 * @date 2018/10/14
 */
public interface Proxy {

    /**
     * 执行链式代理
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws  Throwable;

}
