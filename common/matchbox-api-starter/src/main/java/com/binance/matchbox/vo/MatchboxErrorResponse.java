package com.binance.matchbox.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatchboxErrorResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6991664245830109761L;
    private Long code;
    private String msg;
}
