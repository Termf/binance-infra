package com.binance.matchbox.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by Fei.Huang on 2018/5/30.
 */
@Data
public class TradingAccountDetails {

    private BigDecimal makerCommission;
    private BigDecimal takerCommission;
    private BigDecimal buyerCommission;
    private BigDecimal sellerCommission;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private Long accountId;
    private Long externalId;
    private Date updateTime;
    private List<Balance> balances;

    @Data
    static class Balance {
        private String asset;
        private String free;
        private String locked;
    }

}
