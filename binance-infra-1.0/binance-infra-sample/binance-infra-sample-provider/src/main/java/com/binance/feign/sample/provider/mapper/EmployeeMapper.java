package com.binance.feign.sample.provider.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.binance.feign.sample.provider.domain.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {}