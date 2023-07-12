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
public class MbxPutAccountSymbolsCommissionRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/accountSymbolsCommission";
    }

    private long accountId;
    @NotEmpty
    private List<String> symbols;
    private long buyerCommission;
    private long makerCommission;
    private long sellerCommission;
    private long takerCommission;

    public MbxPutAccountSymbolsCommissionRequest(long accountId, @NotEmpty List<String> symbols, long buyerCommission,
                                                 long makerCommission, long sellerCommission, long takerCommission) {
        this.accountId = accountId;
        this.symbols = symbols;
        this.buyerCommission = buyerCommission;
        this.makerCommission = makerCommission;
        this.sellerCommission = sellerCommission;
        this.takerCommission = takerCommission;
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

    public long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(long takerCommission) {
        this.takerCommission = takerCommission;
    }
}
