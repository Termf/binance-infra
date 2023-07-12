package com.binance.master.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shining.Cai on 2018/09/06.
 **/
public class BeanConverter extends BeanUtils {

    /**
     * instantiate a object and copy value
     * @param source source object
     * @param targetCls target class
     * @return instance of targetCls hold data of source
     */
    public static <T> T createBean(Object source, Class<T> targetCls){
        if (source == null){
            return null;
        }
        T target = instantiate(targetCls);
        copyProperties(source, target);
        return target;
    }

    /**
     *  BeanConverter.createBean 的list版本
     */
    public static <T> List<T> createList(List<? extends Object> sourceList, Class<T> targetCls){
        if (sourceList==null){
            return null;
        }
        List<T> result = new ArrayList<>(sourceList.size());
        for (Object object:sourceList){
            result.add(createBean(object, targetCls));
        }
        return result;
    }
}
