package com.binance.platform.mbx.service.impl;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.master.utils.StringUtils;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.binance.platform.mbx.cloud.consumer.UserApiConsumer;
import com.binance.platform.mbx.cloud.model.GetUserResponse;
import com.binance.platform.mbx.config.MbxCustomProperties;
import com.binance.platform.mbx.constant.MetricsConstants;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.service.AccountService;
import com.binance.platform.monitor.Monitors;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 1:03 下午
 */
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    public static final String METRIC_NAME_CACHE_ACCOUNT_ID = "accountId";
    public static final TimeUnit CACHE_UNIT = TimeUnit.MINUTES;
    private final DelegateMapSize expiringMapSizeGauge;
    private UserApiConsumer userApiConsumer;
    private ExpiringMap<Long, Long> expiringMap;
    private int cacheMaxSize;
    private long cacheMinutes;

    public AccountServiceImpl(UserApiConsumer userApiConsumer, MbxCustomProperties mbxCustomProperties) {
        this.userApiConsumer = userApiConsumer;
        cacheMaxSize = mbxCustomProperties.getAccountIdCacheMaxSize();
        cacheMinutes = mbxCustomProperties.getAccountIdCacheMinutes();
        expiringMapSizeGauge = new DelegateMapSize(this);
        Monitors.gauge(MetricsConstants.METRICS_CACHE_SIZE, expiringMapSizeGauge,
                MetricsConstants.TAG_NAME_CACHE_NAME, METRIC_NAME_CACHE_ACCOUNT_ID);
        initExpiringMap();
    }

    private void initExpiringMap() {
        expiringMap = ExpiringMap.builder()
                .maxSize(cacheMaxSize)
                .expiration(cacheMinutes, CACHE_UNIT)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
    }

    @Override
    public Long retrieveTradingAccountIdByUserId(long userId) {
        Long accountId = retrieveTradingAccountIdByUserIdSilently(userId);
        if (accountId == null) {
            throw new MbxException(GeneralCode.USER_ACCOUNT_NOT_EXIST);
        }

        return accountId;
    }

    @Override
    public Long retrieveTradingAccountIdByUserIdSilently(long userId) {
        Monitors.count(MetricsConstants.METRICS_CACHE_TOTAL, MetricsConstants.TAG_NAME_CACHE_NAME, METRIC_NAME_CACHE_ACCOUNT_ID);
        Long accountId = expiringMap.get(userId);
        if (accountId == null) {
            accountId = retrieveTradingAccountIdWithoutCache(userId);
            if (accountId != null) {
                expiringMap.put(userId, accountId);
            }
        }
        return accountId;
    }

    private Long retrieveTradingAccountIdWithoutCache(long userId) {
        Monitors.count(MetricsConstants.METRICS_CACHE_MISS, MetricsConstants.TAG_NAME_CACHE_NAME, METRIC_NAME_CACHE_ACCOUNT_ID);

        APIResponse<GetUserResponse> response = userApiConsumer.getUserById(userId);
        if (!APIResponse.Status.OK.equals(response.getStatus())
                && (GeneralCode.USER_NOT_EXIST.getCode().equals(response.getCode())
                || GeneralCode.USER_ACCOUNT_NOT_EXIST.getCode().equals(response.getCode()))) {
            return null;
        }
        GetUserResponse getUserResponse = ApiResponseUtil.getAPIRequestResponse(response);

        if (getUserResponse == null || getUserResponse.getUserInfo() == null
                || getUserResponse.getUserInfo().getTradingAccount() == null) {
            return null;
        }

        return getUserResponse.getUserInfo().getTradingAccount();
    }

    @Value("${" + MbxCustomProperties.CACHE_ACCOUNT_ID_CLEAR_NAME + ":}")
    public void clearAccountIdCache(String anyValue) {
        log.info("MBX SDK cache for accountId cleared. size:{}", expiringMap.size());
        ExpiringMap<Long, Long> expiringMap = this.expiringMap;
        initExpiringMap();
        expiringMap.clear();
    }

    @Value("${" + MbxCustomProperties.CACHE_ACCOUNT_ID_MAX_SIZE_NAME + ":" + MbxCustomProperties.DEFAULT_ACCOUNT_ID_CACHE_MAX_SIZE + "}")
    public void setCacheMaxSize(String maxSize) {
        if (StringUtils.isBlank(maxSize)) {
            return;
        }
        try {
            int maxSizeValue = Integer.parseInt(maxSize);
            int currentMaxSize = expiringMap.getMaxSize();
            if (maxSizeValue == currentMaxSize) {
                return;
            }
            expiringMap.setMaxSize(maxSizeValue);
            this.cacheMaxSize = maxSizeValue;
            log.info("MBX SDK cache for accountId Max Size changed from {} to {}", currentMaxSize, maxSizeValue);
        } catch (Exception e) {
            log.error("MBX SDK cache for accountId Max Size changed error", e);
        }
    }

    @Value("${" + MbxCustomProperties.CACHE_ACCOUNT_ID_MINUTES_NAME + ":" + MbxCustomProperties.DEFAULT_ACCOUNT_ID_CACHE_MINUTES + "}")
    public void setCacheDuration(String minutes) {
        if (StringUtils.isBlank(minutes)) {
            return;
        }
        try {
            long minutesValue = Long.parseLong(minutes);
            long currentExpiration = cacheMinutes;
            if (currentExpiration == minutesValue) {
                return;
            }
            this.cacheMinutes = minutesValue;
            log.info("MBX SDK cache for accountId expiration changed from {} to {}, Effective after restart", currentExpiration, minutesValue);
        } catch (Exception e) {
            log.error("MBX SDK cache for accountId expiration changed error", e);
        }
    }

    private static class DelegateMapSize extends Number {
        private final AccountServiceImpl accountService;

        public DelegateMapSize(AccountServiceImpl accountService) {
            this.accountService = accountService;
        }

        @Override
        public int intValue() {
            if (accountService.expiringMap == null) {
                return 0;
            } else {
                return accountService.expiringMap.size();
            }
        }

        @Override
        public long longValue() {
            return intValue();
        }

        @Override
        public float floatValue() {
            return intValue();
        }

        @Override
        public double doubleValue() {
            return intValue();
        }
    }
}
