package com.binance.platform.resilience4j.ddos;

public interface DdosStrategy {

	/**
	 * 是否是Ddos
	 * 
	 * @return
	 */
	Boolean isDdos();

}
