package com.binance.infra.sample.resilience4j.server.controller;

import com.binance.infra.sample.resilience4j.model.Book;
import com.binance.infra.sample.resilience4j.model.Response;
import com.binance.infra.sample.resilience4j.server.custom.ChannelStrategy;
import com.binance.platform.resilience4j.ratelimiter.ServerRateLimiter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-12
 */
@Log4j2
@RestController
@RequestMapping("book")
public class BookController {

    /**
     * 模拟存储业务数据，不使用数据库，防止在限流demo中造成过多依赖
     */
    private static final Map<String, Book> BOOK_STORE = new HashMap<>();

    static {
        for(int i = 0; i < 3; i++){
            String id = "id" + i;
            Book book = new Book(id, "MicroService Edition " + (i + 1), "Lucas", 100 + i*10, "Binance Press Ltd.");
            BOOK_STORE.put(id, book);
        }
    }

    private final Random random = new SecureRandom();

    /**
     * 注意点：
     * 1. 如果采用自定义的strategy，Spring容器中必须存在一个该策略的一个bean，可采用@Component或@Bean皆可
     * 2. limitForPeriod和limitRefreshPeriod两个参数支持apollo配置，key中必须含有ratelimit，否则无法动态更新
     * 3. 采用限流和不限流对比，对qps影响大约在10%所有，开发本机测试结果，可能存在误差
     * 4. useRedis=true表示集群限流，因不断与redis同步数据，理论上一定有影响，建议小流量限流时使用，如10个实例，但是集群限流数为8个
     * 5. useRedis=false表示单机限流，limitForPeriod的值为本机在刷新时间片段内的可接受流量，多实例时，总流量需乘以实例数
     * 6. 目前不支持根据渠道有区别的限流，如针对同一个接口对渠道A限流100，对渠道B限流200
     */
    @GetMapping("all")
    @ServerRateLimiter(strategy = ChannelStrategy.class,
            limitForPeriod = "${find.books.rateLimit:1}",
            limitRefreshPeriod = "1", useRedis = false)
    public Response<Collection<Book>> findAll(){
        log.info("searching all books...");
        return Response.success(BOOK_STORE.values());
    }

    /**
     * Throw exception at a specified rate.
     */
    @GetMapping("exception/{rate}")
    public Response<Collection<Book>> filter(@PathVariable float rate) throws Exception{
        int threshold = (int)Math.ceil(rate * 100);
        int randomValue = random.nextInt(100);
        if(randomValue <= threshold){
            throw new Exception("Exception at rate = " + rate);
        }

        float price = 100;
        return Response.success(BOOK_STORE.values().stream()
                .filter(b -> b.getPrice() >= price).collect(Collectors.toSet()));
    }

    @Value("${server.port}")
    private String port;

    @GetMapping("instance")
    public Response<String> getInstanceInfo(){
        return Response.success("port = " + port);
    }


}
