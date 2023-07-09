package com.binance.infra.sample.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-07-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private String id;
    private String name;
    private String author;
    private float price;
    private String press;
}
