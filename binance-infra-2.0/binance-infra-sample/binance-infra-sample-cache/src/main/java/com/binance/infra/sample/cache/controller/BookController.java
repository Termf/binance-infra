package com.binance.infra.sample.cache.controller;

import com.alicp.jetcache.anno.CacheUpdate;
import com.binance.infra.sample.cache.model.Book;
import com.binance.infra.sample.cache.model.Response;
import com.binance.infra.sample.cache.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-17
 */
@RestController
@RequestMapping("book")

public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("all")
    public Response<Collection<Book>> findAll(){
        return Response.success(bookService.findAll());
    }

    @GetMapping("/jet/{id}")
    public Response<Book> findById(@PathVariable String id){
        return Response.success(bookService.findById(id));
    }

    @GetMapping("/spring/{id}")
    public Response<Book> findByBookId(@PathVariable String id){
        return Response.success(bookService.findByBookId(id));
    }

    @PutMapping("{id}/price/{price}")
    public Response<Book> updatePrice(@PathVariable String id, @PathVariable float price){
        Book book = bookService.findById(id);
        book.setPrice(price);
        return Response.success(bookService.update(book));
    }

}
