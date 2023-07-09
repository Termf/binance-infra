package com.binance.infra.sample.apollo;

import com.binance.infra.sample.apollo.customize.ApolloConfig;
import com.binance.infra.sample.apollo.model.Rule;
import com.binance.infra.sample.apollo.model.ValueGroup;
import com.binance.master.constant.Constant;
import com.binance.master.utils.IPUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示apollo的三种用法
 * 1. @Value
 * 2. @ConfigurationProperties
 * 3. 自定义监听
 *
 * 实例在启动时，会根据当前的环境自动连接apollo服务，地址已在apollo-env.properties内置，
 * 但如果需要连接其他apollo服务时，可设定apollo.meta参数，如下：
 * -Dapollo.meta=http://xxx.yyy.zzz.com:1234
 *
 *
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@SpringBootApplication
@RestController
@RequestMapping("sample")
@Log4j2
public class Application {

    public static void main(String[] args) {
        System.setProperty(Constant.LOCAL_IP, IPUtils.getIp());
        SpringApplication.run(Application.class, args);
    }

    /**
     * 存在apollo的key可以更新到实例中，通过rest接口可验证更改前后变化，
     * 同时也可以观察日志变化，例如：Auto update apollo changed value successfully
     *
     * 如下${sample.string.value}为通过PropertySource更新配置，
     * application.properties和每一个apollo的namespace都是一个PropertySource，
     * 按优先级apollo的namespace优先级更高
     *
     * 也可以给这个属性设置默认值，形如：${sample.string.value: defaultValue}
     * 当没有任何PropertySource设置该值，也没有设置默认值时，启动会报异常
     *
     * 更多用法如：
     * 是否符合某个正则表达式：#{'${sample.string.value}' matches '^[0-9]+$'}
     * 以逗号分割成数组：#{'${sample.string.value}'.split(',')}
     *
     */
    @Value("${sample.string.value}")
    private String stringValue;

    @Value("${sample.int.value}")
    private int intValue;

    @Value("#{'${sample.string.value}' matches '^[0-9]+$'}")
    private boolean isDigital;

    /**
     * 这个map返回能看到intValue是不带引号的，说明是个数字，
     * 而stringValue是带引号的
     */
    @GetMapping("apollo/values")
    public Map<String, Object> getStringValue(){
        Map<String, Object> values = new HashMap<>();
        values.put("stringValue", stringValue);
        values.put("intValue", intValue);
        values.put("isDigital", isDigital);
        return values;
    }

    /**
     * ConfigurationProperties的例子
     */
    @Autowired
    public ValueGroup valueGroup;

    @GetMapping("group")
    public ValueGroup getValueGroup(){
        return valueGroup;
    }

    @Autowired
    public ApolloConfig apolloConfig;

    private List<Rule> rules;

    @PostConstruct
    public void init(){
        apolloConfig.observe("sample.observed.rules", this::parseRules);
    }

    private void parseRules(String json){
        @SuppressWarnings("UnstableApiUsage")
        Type type = new TypeToken<List<Rule>>() {}.getType();
        rules = new Gson().fromJson(json == null ? "[]" : json, type);
        log.info("Successfully loaded {} rule(s).", rules.size());
    }

    /**
     * 如果一个key上的value是一个json，也可以采用自定义监听的方式
     * 好处在于：
     * 1. apollo的修改能热更新到实例中
     * 2. 非apollo的apollo.bootstrap.namespaces默认定义的namespace也能监听，尤其是公共namespace
     * 3. 对于复杂场景，大json的配置，额外的校验方式，自定义更灵活
     */
    @GetMapping("rules")
    public List<Rule> getRules(){
        return rules;
    }



}
