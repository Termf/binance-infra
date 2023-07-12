package com.binance.master.data.sharding;

import io.shardingsphere.api.config.strategy.ShardingStrategyConfiguration;
import io.shardingsphere.core.keygen.KeyGenerator;
import io.shardingsphere.core.rule.DataNode;
import io.shardingsphere.core.util.InlineExpressionParser;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Data
public class TableRuleSharding {

    private String logicTable;

    private String actualDataNodes;

    private ShardingStrategyConfiguration databaseShardingStrategyConfig;

    private ShardingStrategyConfiguration tableShardingStrategyConfig;

    private String keyGeneratorColumnName;

    private KeyGenerator keyGenerator;

    private String logicIndex;

    public void initTable(Map<String, DataSource> dataSourceMap) {
        List<String> actualDataNodes = new InlineExpressionParser(this.getActualDataNodes()).splitAndEvaluate();
        for (String each : actualDataNodes) {
            DataNode dataNode = new DataNode(each);
            DataSource dataSource = dataSourceMap.get(dataNode.getDataSourceName());
            this.checkTable(dataSource, this.getLogicTable(), dataNode.getTableName());
        }
    }

    private void checkTable(DataSource dataSource, String logicTable, String shardingTable) {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            String queryTableSql = String
                    .format("select TABLE_NAME from information_schema.tables WHERE TABLE_NAME = '%s' AND TABLE_SCHEMA = '%s'", shardingTable,connection.getCatalog());
            try(ResultSet resultSet = statement.executeQuery(queryTableSql)){
                if (!resultSet.next()) {
                    String sql = String.format("create table %s like `%s`", shardingTable, logicTable);
                    statement.execute(sql);
                }
            }catch (Exception e){
                throw new SQLException("Create Sharding Table error, Execute sql error", e);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Create Sharding Table Error:" + logicTable, e);
        }
    }

}
