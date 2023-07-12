package com.binance.master.old.data.product;

import com.binance.master.old.config.OldDB;
import com.binance.master.old.models.product.OperationDepartment;
import org.apache.ibatis.annotations.Param;

@OldDB
public interface OperationDepartmentMapper {
    OperationDepartment fetchOperationDepartmentById(@Param("id")Integer id);
}