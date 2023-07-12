package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.annotation.MbxField;
import com.binance.platform.mbx.matchbox.annotation.MbxIgnored;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import com.binance.platform.mbx.util.ArrayValueUtil;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetAccountTPlusLockStateRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/accountTPlusLockState";
    }

    private long accountId;
    @NotEmpty
    private List<String> symbols;

    public MbxGetAccountTPlusLockStateRequest(long accountId, @NotEmpty List<String> symbols) {
        this.accountId = accountId;
        this.symbols = symbols;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @MbxIgnored
    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    @MbxField("symbols")
    public String getActualSymbols() {
        return ArrayValueUtil.convert(symbols);
    }
}
