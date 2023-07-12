package com.binance.platform.mbx.model.product;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * 交易账户信息
 */
public class PriceConvertVo extends ToString {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5251739371643720943L;
	
	private BigDecimal price;
	
	private BigDecimal amount;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
