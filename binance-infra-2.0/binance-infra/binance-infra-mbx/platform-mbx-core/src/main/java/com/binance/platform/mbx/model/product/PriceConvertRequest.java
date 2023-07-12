package com.binance.platform.mbx.model.product;

import com.alibaba.fastjson.annotation.JSONField;
import com.binance.master.commons.ToString;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 获取加个转换对象
 */
public class PriceConvertRequest extends ToString {
    /**
     * 需要转换的自唱
     */
    @NotNull
    private String from;

    /**
     * 目标资产
     */
    @NotNull
    private String to;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //Jackson包
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")  //FastJson包使用注解
    private Date date;

    public PriceConvertRequest(@NotNull String from, @NotNull String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
