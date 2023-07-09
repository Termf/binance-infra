package com.binance.feign.sample.provider.message.receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.binance.feign.sample.provider.message.RabbitMQTopicConfig;

/**
 * topic模式消费者
 * 
 * @author cm_wang
 *
 */
@Component
public class TopicReceiver {
    private static Logger logger = LoggerFactory.getLogger(DirectReceiver.class);

    // queues是指要监听的队列的名字
    @RabbitListener(queues = RabbitMQTopicConfig.TOPIC_QUEUE)
    public void receiveTopic(String obj) {
        logger.info("receiveTopic收到消息:" + obj);
    }
}
