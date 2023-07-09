package com.binance.platform.apollo;

public class SecretsManagerConstants {

    // 这三个值会在common-config / ops.infra.all里维护
    public static final String AWS_ACCESSKEY = "com.binance.aws.accesskey";
    public static final String AWS_SECRETKEY = "com.binance.aws.secretkey";
    public static final String AWS_REGION = "com.binance.aws.region";

    // 这些值需要应用自己的secretName
    public static final String AWS_SECRETNAME = "com.binance.secretsManager.secretName";

    // 这个值在apollo做变更时，触发重新加载
    public static final String AWS_SECRET_REFETCH = "com.binance.secretsManager.refetch";

    // 这个是内网安全加固的secretName
    public static final String AWS_INTRA_SECURITY_SECRET_NAME = "com.binance.intra.security.secretsManager.secretName";

    // 这两个是启动的时候需要加载的
    public static final String SECRETSMANAGER_PROPERTY_SOURCE_NAME = "SecretsManagerPropertySources";

}
