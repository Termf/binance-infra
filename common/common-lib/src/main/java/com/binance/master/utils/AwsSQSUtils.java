package com.binance.master.utils;

import java.util.List;
import java.util.Map;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class AwsSQSUtils {
    private static final Logger logger = LoggerFactory.getLogger(AwsSQSUtils.class);

    private static final ProfileCredentialsProvider PROFILE_CREDENTIALS_PROVIDER = new ProfileCredentialsProvider();

    private static AmazonSQS sqs = null;

    public static void init(String active, String regionName) {

        logger.info("环境变量：" + active);
        logger.info("Region：" + regionName);

        Regions region = Regions.fromName(regionName);

        if (active.contains("local")) {
            try {
                PROFILE_CREDENTIALS_PROVIDER.getCredentials();
            } catch (Exception e) {
                logger.error("AwsSQS init error,can't use queue!pls check file ~/.aws/credentials exist!");
                throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                        + "Please make sure that your credentials file is at the correct "
                        + "location (~/.aws/credentials), and is in valid format.", e);
            }
            sqs = AmazonSQSClientBuilder.standard().withCredentials(PROFILE_CREDENTIALS_PROVIDER).withRegion(region)
                    .build();

            logger.info("Local :AwsSQS init success.");
        } else {
            try {
                if (EcsK8sUtils.isEcs()) {
                    sqs = AmazonSQSClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                } else if (EcsK8sUtils.isK8s()) {
                    sqs = AmazonSQSClientBuilder.standard().withRegion(region)
                            .withCredentials(WebIdentityTokenCredentialsProvider.create()).build();
                } else {
                    sqs = AmazonSQSClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                }
                logger.info("OtherEnv: AwsSQS init success.");
            } catch (SecurityException e) {
                logger.error(
                        "AwsSQS init failed, please contact OPS to assign Role ");
            }
        }

    }

    public static ListQueuesResult getListQueuesResult() {
        checkSqs();
        return sqs.listQueues();
    }

    public static List<String> getListQueueUrl() {
        checkSqs();
        return sqs.listQueues().getQueueUrls();
    }

    public static boolean isQueueUrlExist(String queueUrl) {
        checkSqs();
        for (String url : sqs.listQueues().getQueueUrls()) {
            if (StringUtils.equals(url, queueUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: Create a queue
     * @author zhoulang
     * @date 2018年1月10日 上午11:10:19
     * @parameter
     * @return
     */
    public static String createQueue(String queueName) {
        checkSqs();
        Assert.notNull(queueName, "queueName can't be null: createQueue");
        logger.info("Creating a new SQS queue called {}.", queueName);
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return sqs.createQueue(createQueueRequest).getQueueUrl();
    }

    /**
     * @Description: Send a Message to queue
     * @author zhoulang
     * @date 2018年1月10日 上午11:30:14
     * @parameter
     * @return
     */
    public static void sendMessage(String queueUrl, String message) {
        checkSqs();
        checkQueueUrl(queueUrl);
        Assert.notNull(message, "message can't be null!");
        SendMessageRequest request = new SendMessageRequest(queueUrl, message);
        SendMessageResult result = sqs.sendMessage(request);
        try {
            logger.info("发送到aws队列messageId:"+result.getMessageId());
            logger.info("发送到aws队列requestId:"+result.getSdkResponseMetadata().getRequestId());
        } catch (Exception e) {
            logger.error("发送队列失败，queueURL:{},message:{}",queueUrl,message);
        }
    }

    /**
     * @Description: Send a Message to queue
     * @author zhoulang
     * @date 2018年1月10日 上午11:30:14
     * @parameter
     * @return
     */
    public static void sendMessage(String queueUrl, String message,
            Map<String, MessageAttributeValue> messageAttributes) {
        checkSqs();
        checkQueueUrl(queueUrl);
        Assert.notNull(message, "message can't be null: sendMessage");
        SendMessageRequest request = new SendMessageRequest(queueUrl, message);
        request.setMessageAttributes(messageAttributes);
        sqs.sendMessage(request);
    }

    /**
     * @Description: Receive a messages to queue
     * @author zhoulang
     * @date 2018年1月10日 上午11:30:14
     * @parameter
     * @return
     */
    public static List<Message> receiveMessage(String queueUrl, int maxCount, Integer visibilityTimeout) {
        checkSqs();
        checkQueueUrl(queueUrl);
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(maxCount);// 最大获取maxCount条
        if (visibilityTimeout != null) {
            receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
        }
        return sqs.receiveMessage(receiveMessageRequest).getMessages();
    }

    /**
     * @Description: Delete a message
     * @author zhoulang
     * @date 2018年1月10日 上午11:30:14
     * @parameter
     * @return
     */
    public static void deleteMessage(String queueUrl, Message message) {
        checkSqs();
        checkQueueUrl(queueUrl);
        Assert.notNull(message, "message can't be null: message");
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
    }

    /**
     * @Description: Delete a queue
     * @author zhoulang
     * @date 2018年1月10日 上午11:30:14
     * @parameter
     * @return
     */
    public static void deleteQueue(String queueUrl, Message message) {
        checkSqs();
        checkQueueUrl(queueUrl);
        sqs.deleteQueue(new DeleteQueueRequest(queueUrl));
    }

    public static void checkSqs(){
        Assert.notNull(sqs, "AmazonSQS init error!");
    }
    public static void checkQueueUrl(String queueUrl){
        Assert.notNull(queueUrl, "queueUrl can't be null!");
    }
}
