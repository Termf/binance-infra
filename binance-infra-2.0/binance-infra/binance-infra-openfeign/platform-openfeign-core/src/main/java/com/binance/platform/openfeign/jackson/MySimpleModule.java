package com.binance.platform.openfeign.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class MySimpleModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getTypeId() {
		return MySimpleModule.class;
	}

}
