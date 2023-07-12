package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class PriceConvertRequest extends ToString {
    private static final long serialVersionUID = 5509231845996686562L;
    @NotEmpty
    private String from;
    @NotEmpty
    private String to;
    @NotNull
    private BigDecimal amount;
    @DateTimeFormat(
        pattern = "yyyy-MM-dd"
    )
    private Date date;

    public PriceConvertRequest() {
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Date getDate() {
        return this.date;
    }
}