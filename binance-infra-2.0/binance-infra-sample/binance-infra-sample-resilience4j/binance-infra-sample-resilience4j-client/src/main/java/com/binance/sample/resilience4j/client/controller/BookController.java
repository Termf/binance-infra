package com.binance.sample.resilience4j.client.controller;

import com.binance.infra.sample.resilience4j.model.Book;
import com.binance.infra.sample.resilience4j.model.Response;
import com.binance.sample.resilience4j.client.feign.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-13
 */
@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("exception/{rate}")
    public Response<Collection<Book>> findByPrice(@PathVariable float rate) throws Exception {
        return bookService.findByPrice(rate);
    }

    @RequestMapping("instance")
    public Response<String> getInstanceInfo() throws Exception {
        return bookService.getInstanceInfo();
    }

}
