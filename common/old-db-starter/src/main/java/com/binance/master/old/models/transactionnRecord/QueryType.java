package com.binance.master.old.models.transactionnRecord;

import lombok.Data;

import java.util.Date;

@Data
public class QueryType {
    private Integer type ;
    private Long beginId;
    private Long endId;
    private Date start;

}
