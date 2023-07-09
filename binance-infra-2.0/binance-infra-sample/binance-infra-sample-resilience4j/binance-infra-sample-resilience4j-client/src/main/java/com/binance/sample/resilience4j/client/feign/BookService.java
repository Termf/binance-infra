package com.binance.sample.resilience4j.client.feign;

import com.binance.infra.sample.resilience4j.model.Book;
import com.binance.infra.sample.resilience4j.model.Response;
import com.binance.platform.resilience4j.circuitbreaker.ClientCircuitBreaker;
import com.binance.sample.resilience4j.client.fallback.BookFallBackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@FeignClient(name = "sample-resilience4j-server", path = "book")
public interface BookService {

    @GetMapping("exception/{rate}")
    @ClientCircuitBreaker(failureRateThreshold = "1", waitDurationInOpenState = "1",
            ringBufferSizeInHalfOpenState = "1", ringBufferSizeInClosedState = "1",
            fallback = BookFallBackService.class, ignoreExceptions = {})
    Response<Collection<Book>> findByPrice(@PathVariable("rate") float rate) throws Exception;

    @GetMapping("instance")
    @ClientCircuitBreaker(failureRateThreshold = "50", waitDurationInOpenState = "20",
            ringBufferSizeInHalfOpenState = "10", ringBufferSizeInClosedState = "10",
            fallback = BookFallBackService.class, ignoreExceptions = {})
    Response<String> getInstanceInfo();
}
