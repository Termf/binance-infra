package com.binance.platform.swagger.callrecord;

import java.util.List;

import com.binance.platform.swagger.model.ApiCallRecord;
import com.binance.platform.swagger.model.ApiCallRecordIdentity;


/**
 * @author rony.gu
 * @date 2020/4/18
 */
public interface ApiCallRecordManager {

    List<ApiCallRecord> find(ApiCallRecordIdentity identity);

    boolean saveOfUpdate(ApiCallRecord record);

    boolean delete(ApiCallRecordIdentity identity);

    long countExistingKeys(List<ApiCallRecordIdentity> identities);

    Long getTestPlanApiCnt();

    void setTestPlanApiCnt(long count);

}

