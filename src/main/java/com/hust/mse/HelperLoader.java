package com.hust.mse;

import com.hust.mse.helper.AopHelper;
import com.hust.mse.helper.BeanHelper;
import com.hust.mse.helper.ClassHelper;
import com.hust.mse.helper.ConfigHelper;
import com.hust.mse.helper.ControllerHelper;
import com.hust.mse.helper.IocHelper;
import com.hust.mse.utils.ClassUtil;

/**
 * 初始化
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class HelperLoader {

    public static void init() {
        Class[] classes = {ConfigHelper.class, ClassHelper.class, BeanHelper.class, ControllerHelper.class,
            AopHelper.class, IocHelper.class};

        for (Class clz : classes) {
            ClassUtil.loadClass(clz.getName(), false);
        }
    }

}
