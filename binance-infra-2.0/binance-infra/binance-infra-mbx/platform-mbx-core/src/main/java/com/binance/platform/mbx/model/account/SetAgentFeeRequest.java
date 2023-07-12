package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class SetAgentFeeRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    @NotEmpty
    private String userId;
    @NotNull
    private BigDecimal ratio;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
}