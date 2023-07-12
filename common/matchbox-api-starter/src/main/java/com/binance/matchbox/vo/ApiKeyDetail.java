package com.binance.matchbox.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Shining.Cai on 2018/08/20.
 **/
@Data
public class ApiKeyDetail {

    private Long accountId;
    private Long keyId;
    private String desc;
    private List<String> permissions;
    private List<Rule> rules;

    @Data
    static class Rule{
        private Long ruleId;
        private String ip;
        private Long effectiveFrom;
    }
}
