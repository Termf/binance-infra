package com.binance.platform.mybatis.mybatisplus;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import com.binance.platform.mybatis.handler.encrypt.StringEncryptor;

public class MybatisplusApplicationRunListener implements SpringApplicationRunListener {

    private static final String MYBATIS_TYPE_HANDLERS_PACKAGE = "mybatis-plus.type-handlers-package";

    private static final String MYBATIS_TYPE_HANDLERS_PACKAGE_VALUE = "com.binance.platform.mybatis.handler";

    private static final String ID_TYPE = "mybatis-plus.global-config.db-config.id-type";
    private static final String ID_TYPE_AUTO = "auto";

    public MybatisplusApplicationRunListener(SpringApplication application, String[] args) {}

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        if (this.isPresent("com.baomidou.mybatisplus.core.mapper.BaseMapper")) {
            Properties props = new Properties();
            if (!environment.containsProperty(ID_TYPE)) {
                props.put(ID_TYPE, ID_TYPE_AUTO);
            }
            if (environment.containsProperty(MYBATIS_TYPE_HANDLERS_PACKAGE)) {
                props.put(MYBATIS_TYPE_HANDLERS_PACKAGE,
                    environment.getProperty(MYBATIS_TYPE_HANDLERS_PACKAGE) + "," + MYBATIS_TYPE_HANDLERS_PACKAGE_VALUE);
            } else {
                props.put(MYBATIS_TYPE_HANDLERS_PACKAGE, MYBATIS_TYPE_HANDLERS_PACKAGE_VALUE);
            }
            environment.getPropertySources().addFirst(new PropertiesPropertySource("mybatisplus", props));
            StringEncryptor.environment = environment;
        }
    }

    @Override
    public void starting() {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        // TODO Auto-generated method stub

    }

    private boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

}
