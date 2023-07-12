package com.binance.platform.swagger.callrecord.store;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.binance.platform.swagger.callrecord.ApiCallRecordManager;
import com.binance.platform.swagger.model.ApiCallRecord;
import com.binance.platform.swagger.model.ApiCallRecordIdentity;
import com.google.common.collect.Lists;


/**
 * @author rony.gu
 * @date 2020/4/18
 */
public class RedisApiCallRecordManager implements ApiCallRecordManager {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String serviceName;

    private final static String SEPARATE_FLAG = ":";

    private final static String ALL_TEST_PLAN_API_CNT = "_test_plan_cnt";

    public RedisApiCallRecordManager(ConfigurableApplicationContext context, String serviceName) {
        this.serviceName = serviceName;
        this.redisTemplate = context.getBean("redisTemplate", RedisTemplate.class);
    }

    @Override
    public List<ApiCallRecord> find(ApiCallRecordIdentity identity) {
        if (redisTemplate == null) {
            return Lists.newArrayList();
        }
        String key = getKey(identity);
        Set<Object> apiCallRecords = redisTemplate.opsForZSet().reverseRange(key, 0, 9);
        List<ApiCallRecord> result = Lists.newArrayList();
        try {
            result = apiCallRecords.stream().map(obj -> (ApiCallRecord) obj).collect(Collectors.toList());
        } catch (ClassCastException ex) {
            redisTemplate.delete(key);
        }
        // 只保留10条记录
        redisTemplate.delete(key);
        if (result.size() > 0) {
            result.stream().forEach(r -> {
                redisTemplate.opsForZSet().add(key, r, r.getCallCount());
                redisTemplate.expire(key, 30, TimeUnit.DAYS);
            });
        }
        return result;
    }

    @Override
    public boolean saveOfUpdate(ApiCallRecord record) {
        if (redisTemplate == null) {
            return false;
        }
        try {
            ApiCallRecordIdentity identity = record.getIdentity();
            String key = getKey(identity);
            if (!StringUtils.isEmpty(identity.getId())) {
                Set<Object> apiCallRecords = redisTemplate.opsForZSet().reverseRange(key, 0, 9);
                boolean[] isNew = {true};
                List<ApiCallRecord> allApiCallRecords = apiCallRecords.stream().map(obj -> (ApiCallRecord) obj).map(apiCallRecord -> {
                    if (identity.getId().equals(apiCallRecord.getIdentity().getId())) {
                        isNew[0] = false;
                        // 更新数据
                        if(record.isTestPlan()!=null){
                            apiCallRecord.setTestPlan(record.isTestPlan());
                            apiCallRecord.setTestCaseCode(record.getTestCaseCode());
                        }else {
                            apiCallRecord.setCallCount(record.getCallCount() + 1);
                        }
                    }
                    return apiCallRecord;
                }).collect(Collectors.toList());
                if (allApiCallRecords.size() > 0 || isNew[0]) {
                    redisTemplate.delete(key);
                    if (isNew[0]) {
                        record.setCallCount(1);
                        allApiCallRecords.add(record);
                        // 默认TestCaseCode、isTestPlan
                        Long testPlanApiCnt = getTestPlanApiCnt();
                        if (testPlanApiCnt != null) {
                            setTestPlanApiCnt(testPlanApiCnt + 1);
                        }
                    }
                    allApiCallRecords.stream().forEach(r -> {
                        redisTemplate.opsForZSet().add(key, r, r.getCallCount());
                        redisTemplate.expire(key, 30, TimeUnit.DAYS);
                    });
                    return true;
                }
            } else {
                identity.setId(UUID.randomUUID().toString());
                record.setCallCount(1);
                redisTemplate.expire(key, 30, TimeUnit.DAYS);
                return redisTemplate.opsForZSet().add(key, record, record.getCallCount());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    @Override
    public boolean delete(ApiCallRecordIdentity identity) {
        try {
            String key = getKey(identity);
            Set<Object> apiCallRecords = redisTemplate.opsForZSet().reverseRange(key, 0, 9);
            List<ApiCallRecord> filterApiCallRecords = apiCallRecords.stream().map(obj -> (ApiCallRecord) obj)
                    .filter(record -> !record.getIdentity().getId().equals(identity.getId())).collect(Collectors.toList());
            redisTemplate.delete(key);
            if (filterApiCallRecords.size() > 0) {
                filterApiCallRecords.stream().forEach(record -> redisTemplate.opsForZSet().add(key, record, record.getCallCount()));
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public long countExistingKeys(List<ApiCallRecordIdentity> identities) {
        List<String> keys = identities.stream().map(identity -> getKey(identity)).collect(Collectors.toList());
        return redisTemplate.countExistingKeys(keys);
    }

    @Override
    public Long getTestPlanApiCnt() {
        Object testPlanCnt = redisTemplate.opsForValue().get(serviceName + ALL_TEST_PLAN_API_CNT);
        if (testPlanCnt == null) {
            return null;
        } else {
            return Long.valueOf(testPlanCnt.toString());
        }
    }

    @Override
    public void setTestPlanApiCnt(long count) {
        redisTemplate.opsForValue().set(serviceName + ALL_TEST_PLAN_API_CNT, count, 1, TimeUnit.DAYS);
    }

    private String getKey(ApiCallRecordIdentity identity) {
        return new StringBuilder().append(serviceName).append(SEPARATE_FLAG).append(identity.getEndpointName()).append(SEPARATE_FLAG)
                .append(identity.getMethodName()).toString();
    }


}
