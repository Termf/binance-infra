package com.binance.platform.resilience4j.ddos;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.binance.master.utils.JsonUtils;
import com.binance.master.utils.StringUtils;
import com.binance.platform.apollo.SecretsManagerConstants;
import com.binance.platform.common.AlarmUtil;
import com.binance.platform.env.EnvUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * Reactor for the attacking IP
 *
 * @author Li Fenggang Date: 2020/8/15 10:27 下午
 */
public class DdosReactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DdosReactor.class);

    private static final long WAF_MESSAGE_MAX_DELAY_TIME = TimeUnit.SECONDS.toMillis(1);
    private static final String DDOS_GLOBAL_PROPERTIES_QUEUE_NAME = "com.binance.infra.ddos.queueName";

    private static final int AWS_SQS_BATCH_SIZE = 500;
    private final BlockingQueue<String> ddosIpQueue = new LinkedBlockingQueue<>(1000);
    private final String awsSqsAccessKey;
    private final String awsSqsSecretKey;
    private final String awsSqsRegion;
    private final String awsSqsQueueName;
    private final String applicationName;

    private Set<String> wafSendList = new HashSet<>();
    private AmazonSQS amazonSQS;
    private String amazonQueueUrl;
    private long wafNextSendTime = Long.MAX_VALUE;

    public DdosReactor(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        awsSqsQueueName = environment.getProperty(DDOS_GLOBAL_PROPERTIES_QUEUE_NAME, "tk-dev-waf-ddos-sqs");
        awsSqsRegion = environment.getProperty(SecretsManagerConstants.AWS_REGION, "ap-northeast-1");
        awsSqsAccessKey = environment.getProperty(SecretsManagerConstants.AWS_ACCESSKEY);
        awsSqsSecretKey = environment.getProperty(SecretsManagerConstants.AWS_SECRETKEY);
        applicationName = environment.getProperty("spring.application.name");
        watchDdosIpQueue();
        refreshAwsSqs();
    }

    private void watchDdosIpQueue() {
        ThreadFactory threadFactory =
            new ThreadFactoryBuilder().setNameFormat("WafAttackListener-%d").setDaemon(true).build();
        ThreadPoolExecutor fixThreadPool =
            new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), threadFactory);

        fixThreadPool.submit(() -> {
            while (true) {
                try {
                    if (wafSendList.size() > 0) {
                        String ip =
                            ddosIpQueue.poll(wafNextSendTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                        // send ip when timeout
                        notifyToWaf(ip);
                    } else {
                        String ip = ddosIpQueue.take();
                        if (StringUtils.isNotBlank(ip)) {
                            wafNextSendTime = System.currentTimeMillis() + WAF_MESSAGE_MAX_DELAY_TIME;
                            notifyToWaf(ip);
                        }
                    }
                } catch (Throwable e) {
                    LOGGER.error("Process message to WAF error", e);
                }
            }
        });
    }

    private void notifyToWaf(String ip) {
        if (StringUtils.isNotBlank(ip)) {
            wafSendList.add(ip);
        }
        if (wafSendList.size() >= AWS_SQS_BATCH_SIZE || System.currentTimeMillis() >= wafNextSendTime) {
            String message = JsonUtils.toJsonNotNullKey(wafSendList);
            try {
                SendMessageRequest request = new SendMessageRequest(amazonQueueUrl, message);
                SendMessageResult sendMessageResult = amazonSQS.sendMessage(request);

                LOGGER.info("send ddos ips to WAF. message:{}, MessageId:{}, RequestId:{}, HttpStatusCode:{}", message,
                    sendMessageResult.getMessageId(), sendMessageResult.getSdkResponseMetadata().getRequestId(),
                    sendMessageResult.getSdkHttpMetadata().getHttpStatusCode());
            } catch (Exception e) {
                LOGGER.error("send ddos ips to WAF error. message:" + message, e);
            }
            wafNextSendTime = Long.MAX_VALUE;
            wafSendList.clear();
            sendAlarm(message);
        }
    }

    private void sendAlarm(String ipList) {
        int sampleValue = ThreadLocalRandom.current().nextInt(100);
        if (sampleValue >= 10) {
            return;
        }
        String rejectMessage = String.format("[%s] Ddos ipBand: %s", applicationName, ipList);
        AlarmUtil.alarmTeams(rejectMessage);
    }

    private void refreshAwsSqs() {
        if (StringUtils.isNoneBlank(awsSqsAccessKey, awsSqsSecretKey)) {
            // 本地启动直接使用账号密码
            AWSCredentials credentials = new BasicAWSCredentials(awsSqsAccessKey, awsSqsSecretKey);
            amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsSqsRegion).build();
            LOGGER.info("amazonSQS started with accessKey:{***}, secrectKey:{***}, region:{}", awsSqsRegion);
        } else {
            if (EnvUtil.isEcs()) {
                // 基于role的方式启动，除了dev环境以外全部强制用role的方式来启动
                amazonSQS = AmazonSQSClientBuilder.standard()
                    .withCredentials(new EC2ContainerCredentialsProviderWrapper()).withRegion(awsSqsRegion).build();
            } else {
                amazonSQS = AmazonSQSClientBuilder.standard()
                    .withCredentials(WebIdentityTokenCredentialsProvider.create()).withRegion(awsSqsRegion).build();
            }
            LOGGER.info("amazonSQS started with container role and region:{}", awsSqsRegion);
        }
        amazonQueueUrl = amazonSQS.createQueue(awsSqsQueueName).getQueueUrl();
        LOGGER.info("Amazon queue URL is {}", amazonQueueUrl);
    }

    public void process(String ip) {
        ddosIpQueue.offer(ip);
    }

}