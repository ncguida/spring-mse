package com.hust.mse.bean;

import java.util.Map;

import com.hust.mse.utils.CastUtil;

/**
 * 请求参数对象
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class Param {

    private Map<String,Object>  paramMap ;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

}
