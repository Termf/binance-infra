package com.binance.infra.sample.mybatis.service;

import java.util.List;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
public interface BaseService<T> {

    T insert(T t);

    T update(T t);

    T findById(String id);

    List<T> findAll();

    boolean delete(String id);

    int insertBatch(List<T> list);

}
