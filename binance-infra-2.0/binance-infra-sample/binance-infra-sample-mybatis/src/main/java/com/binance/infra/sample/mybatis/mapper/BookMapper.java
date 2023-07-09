package com.binance.infra.sample.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.binance.infra.sample.mybatis.model.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
