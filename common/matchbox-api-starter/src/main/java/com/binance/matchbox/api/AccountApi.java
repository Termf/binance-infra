package com.binance.matchbox.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binance.master.constant.Constant;
import com.binance.matchbox.support.BeanConfig;
import com.binance.matchbox.vo.CreateTradingAccountResponse;
import com.binance.matchbox.vo.TradingAccountDetails;

@FeignClient(name = Constant.MATCHBOX_WEB_SERVICE, url = "${com.binance.matchbox.api.url}",
    configuration = BeanConfig.class)
public interface AccountApi {

    @GetMapping(value = "/v1/account")
    CreateTradingAccountResponse createTradingAccount(@RequestParam("externalId") Long userId,
        @RequestParam("makerCommission") Long makerCommission, @RequestParam("takerCommission") Long takerCommission,
        @RequestParam("buyerCommission") Long buyerCommission, @RequestParam("sellerCommission") Long sellerCommission)
        throws Exception;

    /**
     * 获取TradingAcount详细信息
     * 
     * @param tradingAccountId
     *            交易账户ID
     * @return
     */
    @GetMapping(value = "/v1/account")
    TradingAccountDetails getDetailsByTradingAccountId(@RequestParam("accountId") Long tradingAccountId);

    /**
     * 设置用户TradingAccount
     * 
     * @param tradingAccountId
     *            交易账户ID
     * @param canTrade
     *            是否可以交易
     * @param canWithdraw
     *            默认传true
     * @param canDeposit
     *            默认传true
     * @return
     */
    @PutMapping(value = "/v1/account")
    Object setTradingAccount(@RequestParam("accountId") Long tradingAccountId,
        @RequestParam("canTrade") boolean canTrade, @RequestParam("canWithdraw") boolean canWithdraw,
        @RequestParam("canDeposit") boolean canDeposit);

    /**
     * 设置用户交易费
     * 
     * @param tradingAccountId
     * @param buyerCommission
     * @param sellerCommission
     * @param takerCommission
     * @param makerCommission
     * @return
     */
    @PutMapping(value = "/v1/account/commission")
    Object setCommission(@RequestParam("accountId") Long tradingAccountId,
        @RequestParam("buyerCommission") Long buyerCommission, @RequestParam("sellerCommission") Long sellerCommission,
        @RequestParam("takerCommission") Long takerCommission, @RequestParam("makerCommission") Long makerCommission);

    /**
     * 使用BNB支付交易手续费
     * 
     */
    @DeleteMapping(value = "/v1/gasOptOut")
    Object openCommissionStatus(@RequestParam("accountId") Long tradingAccountId);

    /**
     * 不使用BNB支付交易手续费
     * 
     */
    @PutMapping(value = "/v1/gasOptOut")
    Object closeCommissionStatus(@RequestParam("accountId") Long tradingAccountId);

    @GetMapping(value = "/v1/accountByExternalId")
    List<TradingAccountDetails> accountByExternalId(@RequestParam("externalId") Long externalId);

    /**
     * 获取accountId
     * 
     * @param userId
     * @param makerCommission
     * @param takerCommission
     * @param buyerCommission
     * @param sellerCommission
     * @param canTrade
     * @return
     */
    @PostMapping(value = "/v1/account")
    CreateTradingAccountResponse getTradingAccountId(@RequestParam("externalId") Long userId,
        @RequestParam("makerCommission") Long makerCommission, @RequestParam("takerCommission") Long takerCommission,
        @RequestParam("buyerCommission") Long buyerCommission, @RequestParam("sellerCommission") Long sellerCommission,
        @RequestParam("canTrade") boolean canTrade);

}
