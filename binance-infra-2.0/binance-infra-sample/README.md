

# Binance基础框架（此处应有名字，如binfra = BINance + INFRA）使用手册

binfra是在Spring Cloud基础上，对其集成、创新、扩展，逐步形成Binance自有的基础框架，
binfra能让开发者使用其模块快速构建自己的分布式业务（微）服务。
这些模块包括：

- 服务注册
- 服务发现
- 服务调用
- 智能路由（灰度路由）
- 负载均衡
- 配置中心
- 限流熔断
- 接口文档
- 服务监控（日志、调用链、指标）
- 消息队列
- 多级缓存
- 分布式锁
- 对象关系映射（ORM）
- 撮合交易
- SAPI

## Quick Guide

一个微服务，基本的能力是业务功能，但除此之外，还需要注册到注册中心，以暴露服务；调用服务前提是要发现对应服务，其次负载均衡；
为使这些集成工作简便化，对于一个微服务可以首先使用如下依赖和简单的配置达到快速启动，

模块pom.xml
```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-web</artifactId>
        </dependency>
```

这里一般不需要指定version，以防止版本冲突和版本升级过程中更改多个版本号，前提是项目根POM中已经管理好了整个基础框架的依赖,

项目pom.xml
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>binance-infra-dependencies</artifactId>
            <version>2.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

创建启动类：
```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty(Constant.LOCAL_IP, IPUtils.getIp());
        SpringApplication.run(Application.class, args);
    }
}
```

配置文件application.properties:
```properties
#最简单的情况下，只需要指定名字，以便让配置中心SDK识别去apollo拉取哪份配置
#spring.application.name指定的名称即是apollo中的appId
spring.application.name=binfra-demo-provider
```

在配置中心apollo创建该项目，注意名称必须同spring.application.name指定的一致，
同时添加配置：
```properties
eureka.client.serviceUrl.defaultZone = http://eureka1.devfdg.net/eureka/,http://eureka2.devfdg.net/eureka/
```

此时，虽然没有任何功能，但是启动后可以观察到，注册中心已经有你的实例信息：

![注册实例信息](./doc/registry-instance.png)

点击图片中的链接，可访问swagger接口定义，

通过访问 http://eureka1.devfdg.net:8761/eureka/apps/BINFRA-DEMO-PROVIDER 获取注册信息。

同时，apollo的实例列表中也有你的ip：

![配置实例信息](./doc/config-instance.png)

## 对外提供Rest接口

参考[Spring文档](https://spring.io/guides/gs/rest-service/)

也可参考Demo中所编写的BookController及其父类BaseController

## 为文档定义接口

框架集成了swagger-ui对外展示接口文档，详细信息可参考[Swagger官网](https://swagger.io/) 
注意：官网有三个产品Editor，Codegen和Swagger-UI，参考swagger-ui即可。

实例启动后，可打开如下地址获取接口文档
```text
http://localhost:port/swagger-ui.html#/
```

在没有使用任何swagger注解的情况下，基本的文档功能已清晰展示，但对文档加上说明可使文档更加丰富，
以下是一些常用注解：

- @ApiOperation: 用于接口方法上
- @ApiModel: 用于接口中所使用到的模型上，一般为Request Object和View Object，Demo中为方便未遵循
- @ApiModelProperty: 用于上述模型的属性

## ORM组件Mybatis/MybatisPlus

### maven依赖
当业务需要持久化时，可选择该组件，使用时，添加依赖如下：
```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-mybatis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>
```

### 业务模型：
```java
@Data
@TableName("t_book")
public class Book extends BaseModel{

    @TableField
    private String name;
    @TableField
    private String press;
    @TableField
    private float price;
    @TableField
    private String author;
    @TableField("book_type")
    private BookType bookType;
    @TableField("cost_price")
    private CryptType costPrice;
}
```

因为封装的原因，TableId藏于了父类中：
```java
@Data
public class BaseModel {

    @TableId(type = IdType.INPUT)
    private String id;
    @TableField("created_at")
    private Long createdAt;
    @TableField("last_modified_at")
    private Long lastModifiedAt;
    @TableField
    private boolean deleted;
}
```

建议关系图（属性业务自定义）：

![Service结构图](./doc/model.png)

### Mapper类：
```java
/**
 *
 * BinfraMapper接口虽然不是必需，但通过在领域Mapper和BaseMapper之间加一层，
 * 将来如果想扩展一些基础方法，可直接应用到所有的领域Mapper中，
 * 反之，因为BaseMapper不是第三方类，无法修改，即便扩展，也要修改所有的子类
 *
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-20
 */
@Mapper
public interface BinfraMapper<T extends BaseModel> extends BaseMapper<T> {
}

@Mapper
public interface BookMapper extends BinfraMapper<Book> {
}
```

建议关系图：

![Service结构图](./doc/mapper.png)

### Service类

service建议继承关系如下，以减少大量的重复代码：

![Service结构图](./doc/service.png)

### Controller类

建议关系图：

![Service结构图](./doc/controller.png)

### 数据库初始化

框架的ORM组件支持读写分离，其配置如下：
```java
@Configuration
public class DbConfig {
    @Bean
    public DynamicDatasourceConnectionProvider dynamicDatasourceConnectionDataProvider() {

        return new DynamicDatasourceConnectionProvider() {

            @Override
            public String writeJdbcUrl() {
                return "jdbc:mysql://localhost:3306/exampledb?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false";
            }

            @Override
            public String writeUserName() {
                return "root";
            }

            @Override
            public String writePassoword() {
                return "xxx";
            }

            @Override
            public String readJdbcUrl() {
                return "jdbc:mysql://localhost:3306/exampledb?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false";
            }

            @Override
            public String readUserName() {
                return "root";
            }

            @Override
            public String readPassoword() {
                return "xxx";
            }

        };
    }
}
```

### 枚举类型扩展

在业务代码中，如果存在枚举类型，并且参与持久化，务必实现getValue方法，以返回持久化值

```java
public enum BookType {

    Computer("计算机类"), Financial("金融类");

    private final String typeName;

    BookType(String typeName){
        this.typeName = typeName;
    }

    public String toString(){
        return typeName;
    }

    /**
     * 枚举类如果在领域模型中使用到，需要返回持久化到数据库的值
     * 某些IDE可能报"is never used"的警告，其在反射中使用，IDE检测不到，可忽略警告
     * @return 需要持久化到数据库的值
     */
    public String getValue(){
        return name();
    }

}
```

### 字段加密

在某些业务场景，如密码字段，需要加密后存储到数据库，可以使用CryptType类型，同时在添加配置如下：

```properties
mybatis.encrypt.password=123
mybatis.encrypt.salt=234
```

### Note

- 数据库密码等敏感信息通过SecretManager管理，请勿在代码或者配置中心直接出现
- Mybatis和MybatisPlus尽量不能混用，尤其在xml形式mapper和注解形式，推荐仅使用注解

## 服务调用

推荐使用Feign作为客户端工具在微服务间调用, 已内置在web模块，不需要额外依赖。

### FeignClient类

FeignClient的定义有以下两种方式：

1. 服务提供方已定义好，通过依赖提供方api

    - 优点： 只需依赖，简单方便，无需接口定义
    - 缺点： 
      - 传递一堆依赖，如果只是几个接口却要引入一个包
      - 在提供方没有定义好的情况下，也可能报错，如URL参数注解@PathVariable中要不要定义名字

2. 自定义FeignClient类

自定义的优缺点正好和方式1相反，建议接口不多时自行定义，接口多时依赖POM

### @EnableFeignClients

该注解一般放在启动置于启动类，能自动扫描到启动类包所在路径，不需要显式指定扫描本模块下FeignClient类所在包，
如果引用的其他组件中也有FeignClient类，则需要显式指定扫描包

### 请求头透传

- feign作为客户端会默认传入所有当前线程服务端请求中的请求头，即透传所有请求头（AbstractGrayHeaderCustomizer）
- 如果自定义PenetrateHttpHeaderHandler，则只透传needPenetrateHeader的请求头

```java
@Component
public class MyPenetrateHttpHeaderHandler implements PenetrateHttpHeaderHandler {

    @Override
    public boolean needPenetrateHeader(String headerKey) {
        return headerKey != null && headerKey.equalsIgnoreCase("x-custom-name");
    }

    @Override
    public String replaceHeader(String headerKey, String headerValue) {
        return headerValue + "-salt";
    }
}
```

### 灰度标记

- Cookie中可以添加gray=SOME-LABEL，Cookie中添加gray将转化为header（GrayCookieAndHeaderFilter）
- Header中可以添加X-GRAY-ENV=SOME-LABEL

### h2c

- 默认feign.jetty.enable开启， 而feign默认使用的httpclient关闭，不熟悉的情况下，禁止修改
- feign请求对象转为jetty请求

### 幂等

- com.binance.intra.idempotence.switch=true
- 非Get请求
- feign方法或class带有@Idempotent注解

Note: 在对resource的计算上似乎有bug：IdempotentInterceptor.parsed，FeignClient上可能带有前缀

### 系统保护

类似熔断功能，如不在白名单列表，所有请求进入都需要校验系统cpu使用率和线程数使用率

需开启开关com.binance.intra.sys-protect.switch

### 接口签名及校验

- 需开启com.binance.intra.security.switch
- Feign方法或class带有@IntranetSafe注解

### 慢调用

- 对于不在白名单之内的应用和URL，慢调用将打印error级别日志

### Note

- 服务提供方应提供一个只依赖feign的provider-api依赖
- 暴露的模型及其他注解不应该有对ORM等第三方库的依赖
- 消费方在EnableFeignClients中应该只Enable依赖的service，而不应该将包下不依赖的service也一并依赖进来
- 中台项目中禁止在暴露的接口URL中使用参数，而应该是个固定URL


## 限流熔断resilience4j

使用限流熔断需依赖如下：

```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-resilience4j</artifactId>
        </dependency>
```

### 限流

#### 服务端限流

如需集群限流，需额外增加redis：
```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-redis</artifactId>
        </dependency>
```

代码实例：
```java
    @GetMapping("all")
    @ServerRateLimiter(strategy = IpRateLimiterStrategy.class,
            limitForPeriod = "${find.books.rateLimit:1}",
            limitRefreshPeriod = "1", useRedis = false)
    public Response<Collection<Book>> findAll(){
        log.info("searching all books...");
        return Response.success(BOOK_STORE.values());
    }
```

注意点：
- 如果采用自定义的strategy，Spring容器中必须存在一个该策略的一个bean，可采用@Component或@Bean皆可
- limitForPeriod和limitRefreshPeriod两个参数支持apollo配置，key中必须含有ratelimit，否则无法动态更新
- 采用限流和不限流对比，对qps影响大约在10%所有，开发本机测试结果，可能存在误差
- useRedis=true表示集群限流，因不断与redis同步数据，理论上一定有影响，建议小流量限流时使用，如10个实例，但是集群限流数为8个
- useRedis=false表示单机限流，limitForPeriod的值为本机在刷新时间片段内的可接受流量，多实例时，总流量需乘以实例数
- 目前不支持根据渠道有区别的限流，如针对同一个接口对渠道A限流100，对渠道B限流200

#### 客户端限流

#### 被限流后的response

http status: 429
```json
{
    "status": "ERROR",
    "type": "GENERAL",
    "code": "000003",
    "errorData": "has trigger ratlimiter",
    "data": null,
    "subData": null,
    "params": null
}
```

### 熔断

#### 熔断原理

熔断状态主要分为三种:
- CLOSED: 关闭，熔断器关闭，意味着所有的请求被**放行**
- OPEN: 打开，熔断器打开，意味着所有的请求被**拒绝**
- HALF_OPEN: 一定数量的请求会被放入，并重新计算失败率，如果失败率超过阈值，则变为打开状态，如果失败率低于阈值，则变为关闭状态
  ![Circuit Break State Machine](./doc/circuit-break-status.jpeg)

另外，还有两个特殊状态：
- DISABLED: 始终允许访问
- FORCED_OPEN: 始终拒绝访问

  ![Circuit Break Detail](./doc/circuit-break-detail.jpeg)

基于此，再来看以下几个参数，就非常简单:
- failureRateThreshold: 熔断器在CLOSED状态转换到OPEN状态的失败率阈值，为1-100的比例值
- waitDurationInOpenState: 熔断器从OPEN状态转变为HALF_OPEN状态等待的时间，单位秒
- ringBufferSizeInHalfOpenState: 熔断器HALF_OPEN状态的缓冲区大小，会限制线程的并发量，例如缓冲区为10则每次只会允许10个请求调用后端服务
- ringBufferSizeInClosedState: 熔断器CLOSED状态的缓冲区大小，不会限制线程的并发量，在熔断器发生状态转换前所有请求都会调用后端服务
- ignoreExceptions: 需要忽略的异常，有时业务特殊的异常可以不被记录在失败中，此时可将这些异常设置在这里忽略

注意：**熔断是以status状态码判断的，普通的异常并不会产生熔断**

#### 服务端熔断

#### 客户端熔断

```java
@ClientCircuitBreaker(failureRateThreshold = "50", waitDurationInOpenState = "20", 
            ringBufferSizeInHalfOpenState = "10", ringBufferSizeInClosedState = "10", 
            fallback = null, ignoreExceptions = {})
Response<Collection<Book>> filter(@PathVariable float rate) throws Exception;
```
本机模拟测试环境：
启动一个server实例，一个client实例，因apollo已配置好，可以不加任何参数，
启动jmeter，单线程 + http request + Constant Throughput Timer（1qps） + JSON Assertion($.code=0)
请求/book/instance，请求后强制关闭server实例
可观察到现象：
1. 起初请求正常返回
2. 关闭server实例后，连续报5个100001001错误，因为 10 * 50%=5，符合预期
3. 紧接着5个失败请求后，为20个fallback，因为设置了waitDurationInOpenState = "20"
4. 之后又有10个尝试请求失败，因为ringBufferSizeInHalfOpenState = "10"

![熔断现象](./doc/circuit-break-jmeter.png)

请求失败时：
```json
{
    "code": "100001001",
    "subData": null,
    "data": null,
    "type": "SYS",
    "params": null,
    "errorData": "System abnormality",
    "status": "ERROR"
}
```

请求fallback时：
```json
{
    "code": 0,
    "data": "I'm in getInstanceInfo fallback.",
    "message": "success"
}
```

使用客户端熔断时需注意：
- fallback: 失败回调类，spring容器中必须存在一个该类的bean以及出入参和方法名一致的方法
- 模拟时应尽量使用小的参数值，以便容易看到现象和方便计算其正确性

### 防DDOS攻击


## 配置中心apollo

### 连接apollo配置服务

**框架代码已自动集成apollo客户端，且内置了各环境的apollo服务地址，如无特殊需要，自需要额外做任何集成**

确有必要时，可自行配置VM参数，如下：
```properties
-Dapollo.meta=http://xxx.yyy.zzz.com:1234
```

同时，实例启动时，需要向apollo获取当前应用的配置信息，应用配置的spring.application.name即为apollo的appId,
可通过搜索查询，若不存在但确有需要时，也可自行创建。

### Apollo的用法

#### @Value中使用

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

@Value另外一种用法是直接置于Listener方法上，当value值变化时，直接能调用该方法：

```java

    @Value("${some.apollo.key:defaultValue}")
    public void configKeyChanged(String value) {
        //put your listener logic here.
    }

```

#### @ConfigurationProperties中使用

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

#### 自定义监听

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

## 缓存

### POM依赖

```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-redis</artifactId>
        </dependency>
```

### 配置

```properties
jetcache.enable=true
```

其他常用配置：

```properties
# 单个cache name的本地一级缓存最大数量，默认1000
jetcache.local.default.limit=500

# 一级缓存写超时，默认为30000（30秒）
jetcache.local.default.expireAfterWriteInMillis=50000
# 一级缓存访问超时， 默认值0，意味着不支持缓存访问超时刷新
jetcache.local.default.expireAfterAccessInMillis=60000


# 二级缓存写超时，默认为30000（30秒）
jetcache.remote.default.expireAfterWriteInMillis=50000
# 二级缓存访问超时， 默认值0，意味着不支持缓存访问超时刷新
jetcache.remote.default.expireAfterAccessInMillis=60000
```

### Note

- 缓存的对象需要实现Serializable接口
- expires为第一次存储到失效的时间，不是最后一次访问到失效的时间
- 不支持类似@CacheEvict(value="BookCache.",allEntries=true)的批量逃逸
- 同时支持Spring内置的Cache和JetCache的注解，但不应同时使用，因为缓存不共享，有可能造成数据不一致和内存溢出


## 分布式锁

### POM依赖

```xml
    <dependency>
         <groupId>com.binance.infra.avengers</groupId>
         <artifactId>platform-starter-redis</artifactId>
    </dependency>
```

### 红锁

```java
    
    @Autowired
    private RedisLock redisLock;
    
    /**
     * 与红锁无异，但key只有一个，锁等待3000ms，超时60s
     */
    @GetMapping("default")
    public Response<String> lock(){
        try {
            System.out.println("trying to lock...");
            redisLock.lock();
        } finally {
            redisLock.unlock();
            System.out.println("lock released.");
        }
        return Response.success("success");
    }

    @GetMapping("redlock")
    public Response<String> tryLock(){
        //100是等待锁获取时间，3000是锁的expireTime，单位是毫秒
        boolean locked = redisLock.tryLock("demo-lock", 100, 3000);
        try {
            if (locked) {
                Thread.sleep(200L);
                // 成功, 处理业务
            } else {
                // 获取锁失败的逻辑
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLock.unlock("demo-lock");
        }
        return Response.success("success");
    }

```

### 一致性Hash锁

```java
    
    @Autowired
    private ConsistentHashJedisLock consistentHashJedisLock;

    @GetMapping("consistent")
    public Response<String> consistentHashLock(){
        //100是等待锁获取时间，3000是锁的expireTime，单位是毫秒
        boolean locked = consistentHashJedisLock.tryLock("demo-consistent-lock", 100, 3000);
        try {
            if (locked) {
                Thread.sleep(200L);
                // 成功, 处理业务
            } else {
                // 获取锁失败的逻辑
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(locked) {
                redisLock.unlock("demo-consistent-lock");
            }
        }
        return Response.success("success");
    }

```

### Note

- 高并发场景下不应使用红锁，job中为防止重复调度可以使用

## 注册和发现

- 实例启动后首次注册不带系统负载信息
- 随后每60s向注册中心注册一次，带当前的系统负载信息
- 客户端每5秒中拉取服务实例
- 优雅关机，通知注册中心本实例状态为down，尽量执行完活跃线程，关闭各种连接（数据库、redis、消息等）

## 客户端负载均衡及智能路由（灰度）支持

### 筛选实例过程

1. 获取Reachable实例列表
2. 判断当前请求的灰度标志
   - 灰度时
        - 过滤出灰度实例
        - 选择warmup的实例
   - 非灰度时
        - 使用envkey来做过滤
        - 使用group和version来匹配
        - 使用系统负载来过滤
3. RoundRobin择一


### 实例设置envFlag的方法

- spring.application.envflag=xxx （实时生效，推荐）
- eureka.instance.metadataMap.envflag=xxx

客户端调用时，走灰度实例的方法：
见 [### 灰度标记](#灰度标记)

### ribbon重试配置

```yaml
ribbon:
     # 服务最大重试次数,不包含第一次请求
     MaxAutoRetries: 1
     # 负载均衡切换次数,如果服务注册列表小于 nextServer count 那么会循环请求  A > B >　A
     MaxAutoRetriesNextServer: 1
     # 是否所有操作都进行重试
     OkToRetryOnAllOperations: false   //如果开启了幂等的注解，该值是可以为true的，否则的话，要慎重开启
```

**多种重试组件不应同时使用，否则可能产生成倍重试的效果**

## Mongo数据库支持

- POM依赖

```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-mongo</artifactId>
        </dependency>
```

- 扩展MongoTemplate，通过AOP修改线程变量，实时修改读库type，可用type如下：
    - primary
    - primaryPreferred
    - secondary
    - secondaryPreferred
    - nearest
- 监控日志可实时开关

```properties
monitor.mongo.log=true
```

- 其他配置参数

```properties
# 因为mongo处理大数据的原因，往往单次的nosql长度会很长，
# 如果全部输出，会影响io性能和直接内存。
# 所以支持配置最大输出长度，从左到右截取前多少字符的nosql，默认值为1000
mongo.command.log.maxlen=1000
# 执行超过多少秒报警,默认为5秒
mongo.alarm.time=3
# 单次批量insert语句超过多少条报警，配置-1或不配置则不报警
mongo.alarm.inserts.limit=5000
# 单次批量update语句超过多少条报警，配置-1或不配置则不报警
mongo.alarm.updates.limit=5000
# update语句in的条件超过多少条报警，配置-1或不配置则不报警
mongo.alarm.update.in.limit=5000
```

## Rabbit消息队列

### POM依赖

消费端和服务端都一样：

```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-rabbit</artifactId>
        </dependency>
```

### 配置

服务端：
```properties
spring.rabbitmq.addresses=amqp://guest:guest@localhost
```

消费端：
```properties
spring.rabbitmq.addresses=amqp://guest:guest@localhost
spring.rabbitmq.gray=false
```

在本机开发环境，默认spring.rabbitmq.gray被设置成了true，不会监听任何queue，如果需要在本机模拟，务必设置为false。

### 实现

消息提供端：

```java
@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 需要延迟时可以加@Delay注解，普遍情况下并不需要
     */
    @Delay(interval = 60L, queue = "queue-normal")
    public void sendBook(String message){
        rabbitTemplate.convertAndSend("my-exchange", "my-route", message);
    }

}
```

消息消费端：

```java
@Component
public class MessageReceiver {

    @RabbitListener(queues = "queue-normal")
    public void processMessage(String message) {
        System.out.println("Vow!!! I receive your message: " + message);
    }

}
```

### 延迟队列实现原理

![delay-queue](./doc/delay-queue.png)

- 消费端监听死信队列
- 生产端生产消息至不被监听的队列，并设置超时时间
- 达到超时时间的消息被移到死信队列

## WebSocket支持

### POM依赖

不管是客户端还是服务端，都需要如下依赖：
```xml
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.binance.infra.avengers</groupId>
            <artifactId>platform-starter-websocket</artifactId>
        </dependency>
```

### 配置

```properties
#作为服务端时需要配置本服务端需要暴露的端口
websocket.server.port=18081
#作为客户端时需要配置需要连接的服务端地址
websocket.client.url=wss://stream.binance.com:9443/ws/!miniTicker@arr
```

### 作为客户端

需要实现WebSocketClientHandler接口并实例化Bean置入Spring容器

```java

@Component
public class MyWebSocketClientHandler implements WebSocketClientHandler {

	@Override
	public void onMessage(WebSocketSession session, String message) {
		//作为客户端实时打印服务端的消息
		System.out.println(message);
	}
}

```

### 作为服务端

```java

@Component
@EnableWebSocketServer
@EnableScheduling
public class WebSocketServerApplication {

    @Autowired
    private WebSocketPushService webSocketPushClient; //自动注入bean，如果有集群会自动开启集群

    @Scheduled(fixedDelay = 1000L)
    public void send() {
        webSocketPushClient.broadcastTextMessage("Current time: " + Calendar.getInstance().getTime());
    }
}
```

### 消息监听

```java

@Component
public class MyWebSocketServerHandler implements WebSocketServerHandler {
    @Override
    public void onMessage(WebSocketSession session, String message) {
        session.sendText(message + "HelloWord");
    }

}
```