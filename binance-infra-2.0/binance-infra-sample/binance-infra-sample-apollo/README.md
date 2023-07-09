
# 本文档演示如何使用apollo配置中心

## 连接apollo配置服务

**框架代码已自动集成apollo客户端，且内置了各环境的apollo服务地址，如无特殊需要，不需要额外做任何集成**

确有必要时，可自行配置VM参数，如下：
```properties
-Dapollo.meta=http://xxx.yyy.zzz.com:1234
```

同时，实例启动时，需要向apollo获取当前应用的配置信息，应用配置的spring.application.name即为apollo的appId,
可通过搜索查询，若不存在但确有需要时，也可自行创建。

## Apollo的用法

### @Value中使用

存在apollo的key可以更新到实例中，通过rest接口可验证更改前后变化，
同时也可以观察日志变化，例如：Auto update apollo changed value successfully

如下${sample.string.value}为通过PropertySource更新配置，
application.properties和每一个apollo的namespace都是一个PropertySource，
按优先级apollo的namespace优先级更高

也可以给这个属性设置默认值，形如：${sample.string.value: defaultValue}
当没有任何PropertySource设置该值，也没有设置默认值时，启动会报异常

更多用法如：
- 是否符合某个正则表达式：#{'${sample.string.value}' matches '^[0-9]+$'}
- 以逗号分割成数组：#{'${sample.string.value}'.split(',')}

```java

    @Value("${sample.string.value}")
    private String stringValue;

    @Value("${sample.int.value}")
    private int intValue;

    @Value("#{'${sample.string.value}' matches '^[0-9]+$'}")
    private boolean isDigital;
```

### @ConfigurationProperties中使用

对于一组具有共同前缀的配置参数，可以通过@ConfigurationProperties注解放入同一个类中，

类中属性可以不再特别指定，如：
```java
@Data
@Component
@ConfigurationProperties("sample.rule")
public class ValueGroup {

    private String id;
    private String name;
    private String expression;
}

```

但需要注意两点：
1. Spring expression不会在这里评估
2. apollo的发布不会热更新（需要时可以扩展ApolloConfig，对前缀监听）

### 自定义监听

大部分场景并不需要自定义监听，但对于如下场景可以使用自定义：
1. apollo的修改能热更新到实例中
2. 非apollo的apollo.bootstrap.namespaces默认定义的namespace也能监听，尤其是公共namespace
3. 对于复杂场景，大json的配置，额外的校验方式，自定义更灵活

监听方式如下：
```java

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

```

同时上面的ApolloConfig类，可以为：
```java
@Component
public class ApolloConfig {

    private Config getConfigService(){
        return ConfigService.getAppConfig();
    }

    /**
     * To observe a key in apollo config center, do handler whenever it changes.
     * @param key the key in apollo namespace(application as default)
     * @param handler what to do when value changes
     */
    public void observe(String key, Consumer<String> handler){
        getConfigService().addChangeListener(event -> {
            if(event.isChanged(key)){
                ConfigChange change = event.getChange(key);
                String json = change.getNewValue();
                handler.accept(json);
            }
        });
        handler.accept(getConfigService().getProperty(key, null));
    }
}
```
