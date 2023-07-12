package com.binance.platform.mybatis;

public interface DynamicDatasourceConnectionProvider {

    public String writeJdbcUrl();

    public String writeUserName();

    public String writePassoword();

    public String readJdbcUrl();

    public String readUserName();

    public String readPassoword();

}
