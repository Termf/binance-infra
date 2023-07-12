package com.binance.platform.mongo.preference;

import org.bson.Document;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;

public class MongoPreferenceTemplate extends MongoTemplate {


    private static ThreadLocal<ReadPreference> mongoReadPreferenceThreadLocal;

    public MongoPreferenceTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
        if (mongoReadPreferenceThreadLocal == null) {
            mongoReadPreferenceThreadLocal = new ThreadLocal<>();
        }
    }

    @Override
    protected MongoCollection<Document> prepareCollection(MongoCollection<Document> collection) {
        ReadPreference readPreference = mongoReadPreferenceThreadLocal.get();
        if (readPreference != null) {
            collection = collection.withReadPreference(readPreference);
            return collection;
        } else {
            return super.prepareCollection(collection);
        }
    }


    public void setReadPreference(ReadPreference readPreference) {
        mongoReadPreferenceThreadLocal.set(readPreference);
    }

    public void removeReadPreference() {
        mongoReadPreferenceThreadLocal.remove();
    }
}
