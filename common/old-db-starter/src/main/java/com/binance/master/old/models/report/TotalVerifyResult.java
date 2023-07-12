package com.binance.master.old.models.report;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TotalVerifyResult {
    private Integer id;

    private String productSymbol;

    private Long fromTranId;

    private Long toTranId;

    private BigDecimal expected;

    private BigDecimal actual;

    private Date createTime;


}