#### 对所有的依赖组件进行了Metrcis打点
* CPU、内存、磁盘空间
* JVM Memory、GC、thread、classes等
* tomcat、HttpRequest
* RestTemplate、Feign
* Hystrix
* 数据库连接池、SQL执行时间、慢SQL检测、事务开启和关闭
* Spring的ThreadPoolTaskExecutor和ThreadPoolTaskScheduler
* Redis
* Kafaka、RabbitMQ
* Log events
#### 支持自定义打点
#### 支持自定义Skywalking的Span
#### 格式化日志的输出格式(所有的日志将会带上Skywalking的TraceId)



# 使用方式

## Maven引入

```xml
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>com.binance.platform.binance-infra</groupId>
				<artifactId>binance-infra-dependencies</artifactId>
				<version>1.0.0-SNAPSHOT</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```

2. 引入 monitor 模块
```xml
      <dependency>
         <groupId>com.bkjk.platform.binance-infra</groupId>
         <artifactId>platform-starter-monitor</artifactId>
      </dependency>
```


# 详细功能示例代码和测试用例

## 手动埋点

通过 Monitors.logEvent(String type, Object param) 埋点后会有两个结果，一是在grafana里可以看到该type的数量，二是在skywalking里可以看到param里的信息

```java
  Monitors.logEvent("starter order", orderModel);
```

## 自定义指标

### @Monitor

这个注解可以加载类或者方法上，效果是统计方法的执行次数、耗时并在Grafana里展示，还有在日志中打印出入参数与耗时。***建议采用此注解***

```java
@Monitor
@Slf4j
@Component
@RabbitListener(queues = "project")
public class ProjectServiceListener {
    @Autowired
    UserService remoteUserService;
    @Autowired
    ProjectService remoteProjectService;
    @Autowired
    InvestService remoteInvestService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @RabbitHandler
    public void handleProjectLendpay(@Payload Project project, @Header(value="XID",required = false) String xid) throws Exception {

        Thread.sleep(1000);
        SpringCloudDtsContext.getContext().setAttachment("XID", xid);
        if (project.getStatus() == ProjectStatusEnum.INVEST_FINISH) {
            remoteInvestService.getOrders(project.getId(), InvestStatusEnum.PAID)
                    .forEach(invest -> {
                        try {
                            remoteUserService.lendpayMoney(invest.getInvestorId(), invest.getBorrowerId(), invest.getAmount());
                            remoteInvestService.lendpay(invest.getId());
                        } catch (Exception ex) {
                            try {
                                log.error("处理放款的时候遇到异常：" + objectMapper.writeValueAsString(invest), ex);
                            } catch (JsonProcessingException e) {

                            }
                        }
                    });
            remoteProjectService.lendpay(project.getId());
        }
    }
}
```

打印日志

```
[2019-02-22 15:06:43.817] [SimpleAsyncTaskExecutor-1] [INFO ] [c.b.p.m.l.aop.GenericControllerAspect] [TID: N/A] - ProjectServiceListener.handleProjectLendpay() called with arguments: project: [Project(id=1, projectId=WYB001, totalAmount=1000000, remainAmount=999992, name=投资项目001年化10%, reason=借款人用于生活, borrowerId=3, borrowerName=借款人1, status=OPEN, createdAt=Tue Oct 09 17:57:58 CST 2018)], xid: [null] called via url: [null]
[2019-02-22 15:06:44.825] [SimpleAsyncTaskExecutor-1] [INFO ] [c.b.p.m.l.aop.GenericControllerAspect] [TID: N/A] - ProjectServiceListener.handleProjectLendpay() took [1007 ms] to complete
[2019-02-22 15:06:44.831] [SimpleAsyncTaskExecutor-1] [INFO ] [c.b.p.m.l.aop.GenericControllerAspect] [TID: N/A] - ProjectServiceListener.handleProjectLendpay() returned: [null]

```

### timer
最简单的方式时在方法上加@Timed注解，比如下面的代码表示统计 allPeople 这个方法的执行次数以及执行时间。`method.timed`时这个指标的默认key值，建议不要修改，这个指标会展示在Grafana上。

```java
@Timed("method.timed")
public List<String> allPeople() {
    try {
        Thread.sleep(200);
    } catch (InterruptedException e) {
        3.1.1-opt
    }
    return people;
}
```

如果要单独统计某段代码而不是一个方法，那么可以用下面这种写法，不用指定percentiles，默认系统会添加p80,p90,p95和p99。

```java
long start=MicrometerUtil.monotonicTime();
Exception ex=null;
try{
    // 业务代码
}catch (Exception e){
    ex=e;
    throw ex;
}finally {
	Monitors.recordNanoSecondAfterStartTime("code.execute.timer",start,"method","trade","type","T01","exception",ex==null?"":ex.getClass().getSimpleName());
}
```

### counter
要单独统计某段代码执行的次数，应当使用counter。

```java

Exception ex=null;
try{
    // 业务代码
}catch (Exception e){
    ex=e;
    throw ex;
}finally {
    Monitors.count(counterName,"method","trade","type","T01","exception",ex==null?"":ex.getClass().getSimpleName());
}
```

### summary

summary 用来统计一组数据的分布情况，和timer的区别时，timer统计的时时间，单位只能是纳秒毫秒等时间单位，而summary统计的时数字，没有单位。可以认为timer是一种特殊的summary。

下面举个例子，我们要对交易做统计，需要知道统计每个支付的金额分布，并且要支持按交易渠道做筛选，那可以按下面这样写

```java
        double[] percentiles=new double[]{0.5,0.8,0.9};
        Monitors.summary("trade",10,percentiles,"channel","wechat");
        Monitors.summary("trade",20,percentiles,"channel","alipay");
```
第一个参数是summary的名字，第二个是支付金额，第三个percentiles表示可以统计50% 80% 90%的支付请求的金额，后面是变长参数，变长参数的数量必须是偶数，上面的例子意思是可以按channel统计，有两个channel分别是alipay和wechat

