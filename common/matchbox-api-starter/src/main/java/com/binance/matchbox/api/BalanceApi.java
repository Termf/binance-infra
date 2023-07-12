package com.binance.matchbox.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binance.master.constant.Constant;
import com.binance.matchbox.support.BeanConfig;
import com.binance.matchbox.vo.CancelAssetWithdrawResponse;

/**
 * Created by Fei.Huang on 2018/5/30.
 */
@FeignClient(name = Constant.MATCHBOX_WEB_SERVICE, url = "${com.binance.matchbox.api.url}",
    configuration = BeanConfig.class)
public interface BalanceApi {

    /**
     * 取消处理中的提币请求
     * 
     * @param tradingAccountId
     *            交易账户ID
     * @param asset
     * @param tranId
     * @param amount
     * @return
     */
    @PostMapping(value = "/v1/balance")
    CancelAssetWithdrawResponse cancelAssetWithdrawing(@RequestParam("accountId") Long tradingAccountId,
        @RequestParam("asset") String asset, @RequestParam("updateId") Long tranId,
        @RequestParam("balanceDelta") String amount);

}
