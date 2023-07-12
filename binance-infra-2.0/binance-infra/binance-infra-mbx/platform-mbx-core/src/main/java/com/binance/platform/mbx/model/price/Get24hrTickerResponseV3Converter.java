package com.binance.platform.mbx.model.price;

import com.binance.platform.mbx.matchbox.model.rest.RestGetTicker24HrResponseV3;

/**
 * @author Li Fenggang
 * Date: 2020/8/7 4:02 下午
 */
public class Get24hrTickerResponseV3Converter {
    public static Get24hrTickerResponseV3 convert(RestGetTicker24HrResponseV3 mbxData) {
        Get24hrTickerResponseV3 get24hrTickerResponseV3 = new Get24hrTickerResponseV3();
        get24hrTickerResponseV3.setSymbol(mbxData.getSymbol());
        get24hrTickerResponseV3.setPriceChange(mbxData.getPriceChange());
        get24hrTickerResponseV3.setPriceChangePercent(mbxData.getPriceChangePercent());
        get24hrTickerResponseV3.setWeightedAvgPrice(mbxData.getWeightedAvgPrice());
        get24hrTickerResponseV3.setPrevClosePrice(mbxData.getPrevClosePrice());
        get24hrTickerResponseV3.setLastPrice(mbxData.getLastPrice());
        get24hrTickerResponseV3.setLastQty(mbxData.getLastQty());
        get24hrTickerResponseV3.setBidPrice(mbxData.getBidPrice());
        get24hrTickerResponseV3.setBidQty(mbxData.getBidQty());
        get24hrTickerResponseV3.setAskPrice(mbxData.getAskPrice());
        get24hrTickerResponseV3.setAskQty(mbxData.getAskQty());
        get24hrTickerResponseV3.setOpenPrice(mbxData.getOpenPrice());
        get24hrTickerResponseV3.setHighPrice(mbxData.getHighPrice());
        get24hrTickerResponseV3.setLowPrice(mbxData.getLowPrice());
        get24hrTickerResponseV3.setVolume(mbxData.getVolume());
        get24hrTickerResponseV3.setQuoteVolume(mbxData.getQuoteVolume());
        get24hrTickerResponseV3.setOpenTime(mbxData.getOpenTime());
        get24hrTickerResponseV3.setCloseTime(mbxData.getCloseTime());
        get24hrTickerResponseV3.setFirstId(mbxData.getFirstId());
        get24hrTickerResponseV3.setLastId(mbxData.getLastId());
        get24hrTickerResponseV3.setCount(mbxData.getCount());
        return get24hrTickerResponseV3;
    }
}
