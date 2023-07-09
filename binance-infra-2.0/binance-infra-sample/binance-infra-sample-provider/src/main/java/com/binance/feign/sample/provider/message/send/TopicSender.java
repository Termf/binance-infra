package com.binance.feign.sample.provider.message.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.feign.sample.provider.message.RabbitMQTopicConfig;
import com.binance.feign.sample.provider.message.receive.DirectReceiver;

/**
 * topic模式生产者
 * 
 * @author cm_wang
 *
 */
@Component
public class TopicSender {
    private static Logger logger = LoggerFactory.getLogger(DirectReceiver.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * topic 模式
     */
    public void sendTopic(String obj) {
        logger.info("sendTopic已发送消息");
        this.amqpTemplate.convertAndSend(RabbitMQTopicConfig.TOPIC_EXCHANGE, "rabbitmq.message", obj);
    }
}
