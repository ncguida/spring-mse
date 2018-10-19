package com.hust.mse.bean;

import com.hust.mse.annotation.Aspect;
import com.hust.mse.annotation.Controller;

/**
 * 返回数据对象
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class Data {

    private  Object model;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
