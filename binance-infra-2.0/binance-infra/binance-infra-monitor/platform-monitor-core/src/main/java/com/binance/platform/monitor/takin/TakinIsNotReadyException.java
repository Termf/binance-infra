package com.binance.platform.monitor.takin;

public class TakinIsNotReadyException extends RuntimeException{
    public TakinIsNotReadyException(){
        super("testing is not ready");
    }
}
