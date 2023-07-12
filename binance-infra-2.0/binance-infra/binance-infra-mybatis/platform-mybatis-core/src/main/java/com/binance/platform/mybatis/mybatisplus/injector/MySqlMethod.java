package com.binance.platform.mybatis.mybatisplus.injector;

import com.baomidou.mybatisplus.annotation.DbType;
import com.google.common.collect.Lists;

import java.util.List;


public enum MySqlMethod {
    /**
     * 批量插入
     */
    INSERT_BATCH_MYSQL("insertBatch", "批量插入",
        "<script>INSERT INTO %s %s VALUES \n<foreach item=\"item\" index=\"index\" collection=\"list\" separator=\",\">%s\n</foreach></script>"),

    INSERT_BATCH_ORACLE("insertBatch", "oracle 批量插入数据",
        "<script>INSERT INTO %s %s \n<foreach item=\"item\" index=\"index\" collection=\"list\" separator=\"UNION ALL\">%s\n</foreach></script>",DbType.ORACLE),
    /**
     * 批量修改
     */
    UPDATE_BATCH_BY_ID_MYSQL("updateBatchById", "mysql 根据ID 批量修改数据",
        "<script>UPDATE %s %s WHERE %s IN (<foreach collection=\"list\" separator=\",\" item=\"i\" index=\"index\">#{i.%s}</foreach>)</script>",DbType.MYSQL,DbType.H2),

    UPDATE_BATCH_BY_ID_ORACLE("updateBatchById", "oracle 根据ID 批量修改数据",
        "<script><foreach collection=\"list\" item=\"item\" index=\"index\" open=\"BEGIN\" close=\";END;\" separator=\";\">UPDATE %s %s WHERE %s=#{item.%s}</foreach></script>",DbType.ORACLE),

    /**
     * 插入或者更新
     */
    SAVE_OR_UPDATE_MYSQL("saveOrUpdate", "插入，如果有则更新",
        "<script>\nINSERT INTO %s %s VALUES %s\nON DUPLICATE KEY UPDATE\n%s</script>",DbType.MYSQL,DbType.H2)

    ;
    private final String method;
    private final String desc;
    private final String sql;
    private final List<DbType> supportDbTypes;

    MySqlMethod(String method, String desc, String sql, DbType... supportDbTypes) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
        this.supportDbTypes = supportDbTypes == null ? Lists.newArrayList(DbType.MYSQL) : Lists.newArrayList(supportDbTypes);
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }

    public List<DbType> getSupportDbTypes() {
        return supportDbTypes;
    }
}
