package com.binance.platform.mongo;

import com.binance.platform.mongo.preference.MongoPreferenceTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoPreferenceProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getName().equals("org.springframework.data.mongodb.core.MongoTemplate")) {
            MongoTemplate mongoTemplate = (MongoTemplate)bean;
            MongoPreferenceTemplate mongoPreferenceTemplate = new MongoPreferenceTemplate(mongoTemplate.getMongoDbFactory(),
                    mongoTemplate.getConverter());
            return mongoPreferenceTemplate;
    }
        return bean;
    }

}
