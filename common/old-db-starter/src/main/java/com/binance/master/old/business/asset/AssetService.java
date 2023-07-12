package com.binance.master.old.business.asset;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.data.asset.AssetMapper;
import com.binance.master.old.ibusiness.asset.IAssetService;
import com.binance.master.old.models.asset.OldAsset;

/**
 * 获取老系统的配置
 * 
 * @author wang-bijie
 *
 */
@Service
public class AssetService implements IAssetService {

    @Resource
    private AssetMapper assetMapper;

    @Override
    @Transactional(value = OldDB.TRANSACTION, propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<OldAsset> getAsset(Map<String, Object> param) {
        return this.assetMapper.selectAssetByMap(param);
    }


}
