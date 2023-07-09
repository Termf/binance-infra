package com.binance.feign.sample.provider.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.binance.platform.openfeign.jackson.BigDecimal2String;

public class Pojo {
	private String name;
	private Date date;
	private BigDecimal value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
