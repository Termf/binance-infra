package com.binance.master.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dozer.DozerBeanMapper;
import org.springframework.cglib.beans.BeanCopier;

import ma.glasnost.orika.MappingException;

/**
 * Created by Fei.Huang on 2018/5/25.
 * 
 * Use {@link #OrikaMapperUtils} instead.
 * 
 * Notice:
 * 
 * dozer 性能不好，请使用OrikaMapperUtils替代
 */
public final class CopyBeanUtils {

    private final static DozerBeanMapper dozer = new DozerBeanMapper();

    private CopyBeanUtils() {}

    /**
     * 深度复制Bean（NestedBean,静态内部类等）
     *
     * @param sourceObject
     * @param targetClass
     * @param <T>
     * @return
     * @throws MappingException
     */
    @Deprecated
    public static <T> T copy(Object sourceObject, Class<T> targetClass) throws MappingException {
        return dozer.map(sourceObject, targetClass);
    }

    /**
     * 快速复制Bean，采用动态代理，性能较<code>copy</code>快很多。
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T fastCopy(Object source, Class<T> targetClass) {
        String beanKey = getKey(source.getClass(), targetClass);
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), targetClass, false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        try {
            T object = targetClass.newInstance();
            copier.copy(source, object, null);
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(
                String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()), e);
        }
    }

    private static final Map<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    private static String getKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }
}
