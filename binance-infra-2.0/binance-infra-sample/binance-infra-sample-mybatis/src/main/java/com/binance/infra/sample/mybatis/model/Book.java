package com.binance.infra.sample.mybatis.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.binance.infra.sample.mybatis.enums.BookType;
import com.binance.platform.mybatis.handler.encrypt.CryptType;
import lombok.Data;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
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
