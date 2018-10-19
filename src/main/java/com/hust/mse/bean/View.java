package com.hust.mse.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class View {

    /**
     * 视图路径
     */
    private String path;

    /**
     * 数据模型
     */
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }

    public View(String path, Map<String, Object> model) {
        this.path = path;
        this.model = model;
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
