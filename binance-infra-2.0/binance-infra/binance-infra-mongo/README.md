# 说明  
* 适配mongo-java-driver, spring-data-mongodb, morphia
* 支持动态打开nosql日志，包含mongo requestid，耗时，traceid，nosql命令
* 支持各类型报警：超时报警，批量(insert/update/in)语句数量超限报警，处理错误报警
* 支持方法级别自定义读取偏好(目前只支持spring-data-mongodb)


# 接入方式

## Maven引入

```xml
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>com.binance.platform.binance-infra</groupId>
				<artifactId>binance-infra-dependencies</artifactId>
				<version>2.0.1-SNAPSHOT</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```

2. 引入 mongo 模块
```xml
    <dependency>
        <groupId>com.binance.infra.avengers</groupId>
        <artifactId>platform-starter-mongo</artifactId>
    </dependency>
```

## 添加命令监听器

* 在初始化MongoClientSettings时，人工添加addCommandListener(new MongoCommandListener())
```java
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applicationName(applicationName)
                .applyConnectionString(new ConnectionString(buildURI()))
                .readPreference(ReadPreference.secondary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.JOURNALED)
                .applyToServerSettings(settings -> settings.heartbeatFrequency(10, TimeUnit.SECONDS))
                .applyToSocketSettings(settings -> settings.connectTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES))
                .retryWrites(true).addCommandListener(new MongoCommandListener())
                .build();
```

# 使用方式

## 打开no sql监控日志
**打开日志配置**

monitor.mongo.log=true

## 日志或报警输出的mongo nosql长度
**因为mongo处理大数据的原因，往往单次的nosql长度会很长，如果全部输出，会影响io性能和直接内存。所以支持配置最大输出长度，从左到右截取前多少字符的nosql，默认值为1000**

mongo.command.log.maxlen=1000

## 报警配置
**配置apollo**

**执行超过多少秒报警,默认为5秒**

mongo.alarm.time=3

**单次批量insert语句超过多少条报警，配置-1或不配置则不报警**

mongo.alarm.inserts.limit=5000

**单次批量update语句超过多少条报警，配置-1或不配置则不报警**

mongo.alarm.updates.limit=5000

**update语句in的条件超过多少条报警，配置-1或不配置则不报警**

mongo.alarm.update.in.limit=5000


## 支持方法级别自定义读取偏好(目前只支持spring-data-mongodb)
* 在某些情况下，需要支持方法级别自定义读取偏好，比如某些数据的读取要更实时，直接从主库读而不是从库读
* 使用方式如下，在使用MongoTemplate的类的调用方法上加入注解MongoReadPreference，type值支持：primary,primaryPreferred,secondary,secondaryPreferred,nearest
```java
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @MongoReadPreference(type="primary")
    public <T> List<T> find(Query query, Class<T> entityClass) {
        return mongoTemplate.find(query,entityClass);
    }
```