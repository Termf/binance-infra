package com.binance.feign.sample.provider.message.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.feign.sample.provider.message.RabbitMQDirectConfig;
import com.binance.feign.sample.provider.message.receive.DirectReceiver;

/**
 * direct模式生产者
 * 
 * @author cm_wang
 *
 */
@Component
public class DirectSender {
    private static Logger logger = LoggerFactory.getLogger(DirectReceiver.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendDirect(String obj) {
        logger.info("sendDirectQueue已发送消息");
        // 第一个参数是指要发送到哪个队列里面， 第二个参数是指要发送的内容
        this.amqpTemplate.convertAndSend(RabbitMQDirectConfig.DIRECT_QUEUE, obj);
    }
}
