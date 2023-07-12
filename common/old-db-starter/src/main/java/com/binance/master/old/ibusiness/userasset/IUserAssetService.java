package com.binance.master.old.ibusiness.userasset;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Lip
 *
 */
public interface IUserAssetService {

    public List<Map<String, Object>> getCheckWithdrawing(Map<String, Object> param);

    public List<Map<String, Object>> getVerifyWithdrawing(Map<String, Object> param);

}
