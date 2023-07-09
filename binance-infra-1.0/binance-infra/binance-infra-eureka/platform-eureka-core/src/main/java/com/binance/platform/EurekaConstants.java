
package com.binance.platform;

public class EurekaConstants {

    public static final String PROVIDER_INSTANCE_GROUP = "spring.application.group";
    public static final String PROVIDER_INSTANCE_VERSION = "spring.application.version";
    public static final String PROVIDER_INSTANCE_ENVKEY = "spring.application.envflag";
    public static final String PROVIDER_INSTANCE_GRAYFLOW = "spring.application.grayflow";

    public static final String EUREKA_METADATA_GROUP = "GROUP";
    public static final String EUREKA_METADATA_VERSION = "VERSION";
    public static final String EUREKA_METADATA_ENVKEY = "envflag";
    public static final String EUREKA_METADATA_SERVERLOAD = "SERVERLOAD";
    public static final String EUREKA_METADATA_MANAGEMENT_URL = "management.url";
    public static final String EUREKA_METADATA_GRAYFLOW = "grayflow";// 是否开启灰度流量进入

    public static final String SPRING_BOOT_VERSION = "springboot.version";

    public static final String CONSUMER_INSTANCE_REFERENCE = "eureka.client.reference";

    public static final String CONSUMER_INSTANCE_REFERENCE_SERVICEID = "serviceId";

    public static final String CONSUMER_INSTANCE_REFERENCE_GROUP = EUREKA_METADATA_GROUP.toLowerCase();

    public static final String CONSUMER_INSTANCE_REFERENCE_VERSION = EUREKA_METADATA_VERSION.toLowerCase();

    public static final String CONSUMER_INSTANCE_REFERENCE_ENVKEY = EUREKA_METADATA_ENVKEY.toLowerCase();

    // 默认的envflag的标签
    public static final String EUREKA_META_ENVKEY_DEFAULT = "normal";

}
