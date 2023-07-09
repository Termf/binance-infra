package com.binance.feign.sample.provider.message.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.feign.sample.provider.message.RabbitMQFanoutConfig;
import com.binance.feign.sample.provider.message.receive.DirectReceiver;

/**
 * fanout模式生产者
 * 
 * @author cm_wang
 *
 */
@Component
public class FanoutSender {
    private static Logger logger = LoggerFactory.getLogger(DirectReceiver.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendFanout(String obj) {
        logger.info("sendFanout已发送消息");
        this.amqpTemplate.convertAndSend(RabbitMQFanoutConfig.FANOUT_EXCHANGE, "", obj);
    }
}
