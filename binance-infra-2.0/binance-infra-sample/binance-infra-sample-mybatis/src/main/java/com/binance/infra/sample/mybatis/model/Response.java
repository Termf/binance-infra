package com.binance.infra.sample.mybatis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-07-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAILURE = "failure";

    private int code;
    private String message = MSG_SUCCESS;
    private T data;

    public static <T> Response<T> success(T data){
        return new Response<>(0, MSG_SUCCESS, data);
    }

    public static <T> Response<T> failure(int code, String message){
        return new Response<>(code, message, null);
    }
}
