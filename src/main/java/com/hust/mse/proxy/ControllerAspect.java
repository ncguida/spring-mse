package com.hust.mse.proxy;

import java.lang.reflect.Method;

import com.hust.mse.annotation.Aspect;
import com.hust.mse.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对 注解了Controllerle类
 *
 * @author ncguida
 * @date 2018/10/14
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private final static Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    @Override
    protected void before(Class<?> targetClass, Method targetMethod, Object[] params) throws Throwable {
        LOGGER.debug("----------begin------------");
        LOGGER.debug("class:" + targetClass.getName());
        LOGGER.debug("method:" + targetClass.getName());
        begin = System.currentTimeMillis();
    }


    @Override
    protected void after(Class<?> targetClass, Method targetMethod, Object[] params) {
        LOGGER.debug("costTime:" + (System.currentTimeMillis() - begin));
        LOGGER.debug("-----------end---------------");
    }
}
