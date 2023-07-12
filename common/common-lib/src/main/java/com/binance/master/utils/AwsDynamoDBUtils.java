package com.binance.master.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;

public class AwsDynamoDBUtils {
    private static final Logger logger = LoggerFactory.getLogger(AwsDynamoDBUtils.class);

    private static final ProfileCredentialsProvider PROFILE_CREDENTIALS_PROVIDER = new ProfileCredentialsProvider();

    static AmazonDynamoDB dynamoDB = null;

    public static void init(String active, String regionName) {

        logger.info("环境变量：" + active);
        logger.info("Region：" + regionName);

        Regions region = Regions.fromName(regionName);

        if (active.contains("local")) {

            try {
                PROFILE_CREDENTIALS_PROVIDER.getCredentials();
            } catch (Exception e) {
                logger.error("AwsDynamoDB init error,can't use queue!pls check file ~/.aws/credentials exist!");
                throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                        + "Please make sure that your credentials file is at the correct "
                        + "location (~/.aws/credentials), and is in valid format.", e);
            }
            dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(PROFILE_CREDENTIALS_PROVIDER)
                    .withRegion(region).build();


            logger.info("Local :AwsDynamoDB init success.");
        } else {
            try {
                if (EcsK8sUtils.isEcs()) {
                    dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                } else if (EcsK8sUtils.isK8s()) {
                    dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region)
                            .withCredentials(WebIdentityTokenCredentialsProvider.create()).build();
                } else {
                    dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                }
                logger.info("OtherEnv: AwsDynamoDB init success.");
            } catch (SecurityException e) {
                logger.error(
                        "DynamoDB init failed, please contact OPS to assign Role ");
            }
        }
    }


    /**
     * @Description: Create a table
     * @author zhoulang
     * @date 2018年1月10日 上午11:10:19
     * @parameter
     * @return
     * @throws InterruptedException
     * @throws TableNeverTransitionedToStateException
     */
    public static void createTable(String tableName, String primeKey, List<String> indexColoumn, String orderColoumn)
            throws TableNeverTransitionedToStateException, InterruptedException {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: createTable");
        Assert.notNull(tableName, "tableName can't be null: createTable");
        Assert.notNull(primeKey, "primeKey can't be null: createTable");

        logger.info("Creating a table name called {}.", tableName);
        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withKeySchema(new KeySchemaElement().withAttributeName(primeKey).withKeyType(KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition().withAttributeName(primeKey).withAttributeType(ScalarAttributeType.S))
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
        if (StringUtils.isNotBlank(orderColoumn)) {
            createTableRequest
                    .withKeySchema(new KeySchemaElement().withAttributeName(orderColoumn).withKeyType(KeyType.RANGE))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName(orderColoumn)
                            .withAttributeType(ScalarAttributeType.S));
        }
        for (String index : indexColoumn) {
            createTableRequest
                    .withGlobalSecondaryIndexes(new GlobalSecondaryIndex().withIndexName(index + "_index")
                            .withKeySchema(new KeySchemaElement().withAttributeName(index).withKeyType(KeyType.HASH))
                            .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
                            .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L)))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName(index)
                            .withAttributeType(ScalarAttributeType.S));
        }

        // Create table if it does not exist yet
        TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
        // wait for the table to move into ACTIVE state
        TableUtils.waitUntilActive(dynamoDB, tableName);
    }

    public static TableDescription tableDescription(String tableName) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: tableDescription");
        Assert.notNull(tableName, "tableName can't be null: tableDescription");
        // Describe our new table
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        return dynamoDB.describeTable(describeTableRequest).getTable();
    }

    public static PutItemResult putItem(String tableName, Map<String, AttributeValue> item) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: putItem");
        Assert.notNull(tableName, "tableName can't be null: putItem");
        Assert.notNull(item, "item can't be null!");
        // Add an item
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        return dynamoDB.putItem(putItemRequest);
    }

    public static String updateItem(String tableName, Map<String, AttributeValue> key,
            Map<String, AttributeValueUpdate> attributeUpdates) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: updateItem");
        Assert.notNull(tableName, "tableName can't be null: updateItem");
        Assert.notNull(key, "key can't be null: updateItem");
        Assert.notNull(attributeUpdates, "attributeUpdates can't be null: updateItem");
        // Add an item
        String returnValues = null;
        UpdateItemRequest updateItemRequest = new UpdateItemRequest(tableName, key, attributeUpdates, returnValues);
        dynamoDB.updateItem(updateItemRequest);
        return returnValues;
    }

    public static GetItemResult getItem(String tableName, Map<String, AttributeValue> key) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: getItem");
        Assert.notNull(tableName, "tableName can't be null: getItem");
        Assert.notNull(key, "key can't be null: getItem");
        GetItemRequest getItemRequest = new GetItemRequest(tableName, key);
        getItemRequest.setConsistentRead(true);
        return dynamoDB.getItem(getItemRequest);
    }

    public static ScanResult queryItem(String tableName, HashMap<String, Condition> filter) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: queryItem");
        Assert.notNull(tableName, "tableName can't be null: queryItem");
        Assert.notNull(filter, "filter can't be null: queryItem");
        ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(filter);

        return dynamoDB.scan(scanRequest);
    }

    public static ScanResult queryItemByFilters(String tableName, List<HashMap<String, Condition>> filters,
            Integer limit, Integer page) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: queryItemByFilters");
        Assert.notNull(tableName, "tableName can't be null: queryItemByFilters");
        Assert.notNull(filters, "filter can't be null: queryItemByFilters");
        ScanRequest scanRequest = new ScanRequest(tableName);

        for (HashMap<String, Condition> filter : filters) {
            scanRequest.withScanFilter(filter);
        }

        if (limit > 0) {
            scanRequest.withLimit(limit);
        }

        ScanResult sacn = dynamoDB.scan(scanRequest);

        List<Map<String, AttributeValue>> thisItems = null;

        if (page == 0) {
            thisItems = sacn.getItems();
        }

        int i = 0;
        int countTotal = sacn.getCount();
        while (sacn.getLastEvaluatedKey() != null) {
            i++;

            scanRequest.setExclusiveStartKey(sacn.getLastEvaluatedKey());
            sacn = dynamoDB.scan(scanRequest);
            if (page == i) {
                thisItems = sacn.getItems();
            }
            countTotal += sacn.getCount();
        }

        sacn.setItems(thisItems);
        sacn.setCount(countTotal);

        return sacn;
    }

    public static ScanResult queryItemByPage(String tableName, String indexName, HashMap<String, Condition> filter,
            Map<String, AttributeValue> map) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: queryItemByPage");
        Assert.notNull(tableName, "tableName can't be null: queryItemByPage");
        Assert.notNull(filter, "filter can't be null: queryItemByPage");
        ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(filter);

        if (indexName != null) {
            scanRequest.withIndexName(indexName);
        }

        if (map != null) {
            scanRequest.withExclusiveStartKey(map);
        }

        return dynamoDB.scan(scanRequest);
    }

    public static QueryResult queryItemByQuery(String tableName, String indexName,
            HashMap<String, Condition> keyCondition, Map<String, Condition> queryFilter) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: queryItemByQuery");
        Assert.notNull(tableName, "tableName can't be null: queryItemByQuery");
        Assert.notNull(keyCondition, "filter can't be null: queryItemByQuery");
        QueryRequest scanRequest = new QueryRequest().withTableName(tableName);

        if (indexName != null) {
            scanRequest.withIndexName(indexName).withKeyConditions(keyCondition);
        }

        if (queryFilter != null) {
            scanRequest.withQueryFilter(queryFilter);
        }

        return dynamoDB.query(scanRequest);
    }

    public static QueryResult queryItemByQureyAndPage(String tableName, String indexName,
            HashMap<String, Condition> keyCondition, Map<String, Condition> queryFilter, Integer limit, Integer page,Boolean isAsc) {
        Assert.notNull(dynamoDB, "AwsDynamoDB init error: queryItemByQureyAndPage");
        Assert.notNull(tableName, "tableName can't be null: queryItemByQureyAndPage");
        Assert.notNull(keyCondition, "filter can't be null: queryItemByQureyAndPage");
        QueryRequest scanRequest = new QueryRequest().withTableName(tableName);

        if (indexName != null) {
            scanRequest.withIndexName(indexName).withKeyConditions(keyCondition);
        }
        
        if(!isAsc) {
            scanRequest.withScanIndexForward(isAsc);
        }
        
        if (queryFilter != null) {
            scanRequest.withQueryFilter(queryFilter);
        }

        // if (limit > 0) {
        // scanRequest.withLimit(limit);
        // }

        QueryResult query = dynamoDB.query(scanRequest);

        List<Map<String, AttributeValue>> allItems = query.getItems();
        List<Map<String, AttributeValue>> thisItems = query.getItems();
        int countTotal = query.getCount();
        while (query.getLastEvaluatedKey() != null) {
            scanRequest.setExclusiveStartKey(query.getLastEvaluatedKey());
            query = dynamoDB.query(scanRequest);

            allItems.addAll(query.getItems());

            countTotal += query.getCount();
        }

        int lastIndex = limit * page;

        if (limit > 0 && page > 0) {
            if (lastIndex > allItems.size()) {
                thisItems = allItems.subList(limit * (page-1), allItems.size());
            } else {
                thisItems = allItems.subList(limit *(page-1), limit * page);
            }
        } else {
            query.setItems(allItems);
            query.setCount(countTotal);

            return query;
        }

        query.setItems(thisItems);
        query.setCount(countTotal);

        return query;
    }


    public static Map<String, AttributeValue> convertAttributeMap(Map<String, Object> params) {
        if (params == null) {
            return null;
        }
        Map<String, AttributeValue> result = new HashMap<String, AttributeValue>();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Boolean) {
                result.put(entry.getKey(), new AttributeValue(((Boolean) entry.getValue()).toString()));
            } else if (entry.getValue() instanceof Long) {
                result.put(entry.getKey(), new AttributeValue().withN(((Long) entry.getValue()).toString()));
            } else if (StringUtils.isNotBlank((String) entry.getValue())) {
                result.put(entry.getKey(), new AttributeValue((String) entry.getValue()));
            }

        }
        return result;
    }
}
