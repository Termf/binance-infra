package com.binance.master.old.data.order;



import java.util.List;
import java.util.Map;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.order.OldOpenOrder;
import com.binance.master.old.models.order.OldOrderHistory;

@OldDB
public interface OldOpenOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OldOpenOrder record);

    int insertSelective(OldOpenOrder record);

    OldOpenOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OldOpenOrder record);

    int updateByPrimaryKey(OldOpenOrder record);

    OldOpenOrder getOpenOrder(Map<String, Object> param);

    int deleteOpenOrder(Map<String, Object> param);

    int addOrUpdateOpenOrder(OldOrderHistory order);

    int updateOpenExecutedAmount(Map<String, Object> param);

    List<OldOpenOrder> selectPage(Map<String, Object> param);

    int selectCount() ;

}
