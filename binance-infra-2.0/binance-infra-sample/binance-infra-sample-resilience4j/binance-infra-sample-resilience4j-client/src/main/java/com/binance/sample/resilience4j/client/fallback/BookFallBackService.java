package com.binance.sample.resilience4j.client.fallback;

import com.binance.infra.sample.resilience4j.model.Book;
import com.binance.infra.sample.resilience4j.model.Response;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@Component
public class BookFallBackService {

    public Response<Collection<Book>> findByPrice(float rate){
        System.out.println("I'm in fall back...");
        return Response.success(Collections.singleton(new Book("000", "hello", "Lucas", 50, "Binance Press")));
    }

    public Response<String> getInstanceInfo(){
        return Response.success("I'm in getInstanceInfo fallback.");
    }
}
