package com.binance.platform.mongo.preference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MongoReadPreference {

    /**
     * mongo read preference type
     *
     * @return
     */
    String type();
}
