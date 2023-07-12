package com.binance.matchbox.vo;

import lombok.Data;

/**
 * 创建api key的结果
 * Created by Shining.Cai on 2018/08/17.
 **/
@Data
public class CreateApiKeyResponse {

    private String apiKey;
    private String secretKey;
    private Long keyId;
}
