package com.binance.platform.mybatis.mybatisplus.injector;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.binance.platform.mybatis.mybatisplus.injector.methods.InsertBatch;
import com.binance.platform.mybatis.mybatisplus.injector.methods.SaveOrUpdate;
import com.binance.platform.mybatis.mybatisplus.injector.methods.UpdateBatchById;

public class MySqlInjector extends DefaultSqlInjector {

	private final DataSource dataSource;

	public MySqlInjector(List<DataSource> dataSource) {
		this.dataSource = dataSource.get(0);
	}

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		List<AbstractMethod> methodArrayList = new ArrayList<>();
		methodArrayList.addAll(super.getMethodList(mapperClass));
		methodArrayList.addAll(Stream.of(//
				new InsertBatch(dataSource), //
				new SaveOrUpdate(dataSource), //
				new UpdateBatchById(dataSource)//
		).collect(toList()));
		return methodArrayList;
	}

}
