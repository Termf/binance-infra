package com.binance.infra.sample.mybatis.controller;

import com.binance.infra.sample.mybatis.model.Book;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
@RestController
@RequestMapping("book")
public class BookController extends BaseController<Book>{


}
