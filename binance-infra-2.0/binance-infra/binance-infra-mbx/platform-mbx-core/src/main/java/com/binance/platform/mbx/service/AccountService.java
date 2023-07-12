package com.binance.platform.mbx.service;

import com.binance.platform.mbx.exception.MbxException;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 1:00 下午
 */
public interface AccountService {
    /**
     * retrieve tradingAccountId, throw {@link MbxException} if the account doesn't exist.
     *
     * @param userId
     * @return
     */
    Long retrieveTradingAccountIdByUserId(long userId);

    /**
     * retrieve tradingAccountId, return null if the account doesn't exist.
     *
     * @param userId
     * @return
     */
    Long retrieveTradingAccountIdByUserIdSilently(long userId);
}
