package com.binance.infra.sample.mybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
@Data
public class BaseModel {

    @TableId(type = IdType.INPUT)
    private String id;
    @TableField("created_at")
    private Long createdAt;
    @TableField("last_modified_at")
    private Long lastModifiedAt;
    @TableField
    private boolean deleted;

}
