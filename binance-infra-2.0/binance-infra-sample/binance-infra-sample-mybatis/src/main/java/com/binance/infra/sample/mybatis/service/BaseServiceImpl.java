package com.binance.infra.sample.mybatis.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.binance.infra.sample.mybatis.model.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
public class BaseServiceImpl<T extends BaseModel> implements BaseService<T>{

    @Autowired
    private BaseMapper<T> mapper;

    @Override
    public T insert(T t) {
        t.setId(UUID.randomUUID().toString());
        t.setCreatedAt(System.currentTimeMillis());
        mapper.insert(t);
        return t;
    }

    @Override
    public T update(T t) {
        if(t.getId() == null || t.getId().trim().equalsIgnoreCase("")){
            throw new IllegalArgumentException("Id must be specified.");
        }
        t.setLastModifiedAt(System.currentTimeMillis());
        int updatedCount = mapper.updateById(t);
        if(updatedCount == 1){
            return t;
        } else {
            throw new IllegalArgumentException("No such id = " + t.getId());
        }
    }

    @Override
    public T findById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public boolean delete(String id) {
        T t = mapper.selectById(id);
        t.setDeleted(true);
        update(t);
        return true;
    }

    @Override
    public int insertBatch(List<T> list) {
        return 0;
    }

}
