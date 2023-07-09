package com.binance.infra.sample.cache.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.binance.infra.sample.cache.model.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-17
 */
@Service
public class BookService {

    private static final Map<String, Book> BOOK_STORE = new HashMap<>();

    static {
        for(int i = 0; i < 30; i++){
            String id = "id" + (i + 1);
            Book book = new Book(id, "MicroService Edition " + (i + 1), "Lucas", 100 + i*10, "Binance Press Ltd.");
            BOOK_STORE.put(id, book);
        }
    }

    @Cached(name = "BookCache.", key = "#id", cacheType = CacheType.BOTH, expire = 3600)
    public Book findById(String id){
        return BOOK_STORE.get(id);
    }

    /**
     * 实际应用过程中，spring原生cache实现不应和jetcache同时使用，没必要，因为：
     * 1. 本身数据并未共享
     * 2. 增加造成内存不够用的几率
     * 且除了老代码的兼容，没有任何其他好处。
     *
     */
    @Cacheable(cacheNames = "BookCache.", key = "#id")
    public Book findByBookId(String id){
        return BOOK_STORE.get(id);
    }

    @CacheUpdate(name = "BookCache.", key = "#id", value = "#book")
    public Book update(Book book){
        BOOK_STORE.put(book.getId(), book);
        return book;
    }

    public List<Book> findAll(){
        return new ArrayList<>(BOOK_STORE.values());
    }

}
