package com.hust.mse.test;

import java.util.ArrayList;
import java.util.List;

import com.hust.mse.annotation.Service;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/12
 */
@Service("test")
public class Test {

    public static void main(String[] args) {
        List<String> list  = new ArrayList<>();

        Class<? extends List> aClass = list.getClass();

        System.out.println(1);

    }
}
