package com.binance.master.old.ibusiness.asset;

import java.util.List;
import java.util.Map;

import com.binance.master.old.models.asset.OldAsset;

/**
 * 
 * @author Lip
 *
 */
public interface IAssetService {

    public List<OldAsset> getAsset(Map<String, Object> param);

}
