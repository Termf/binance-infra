
package com.binance.platform.rabbit.extend;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.core.env.Environment;

import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.rabbit.logger.ReceiveTraceLog;
import com.rabbitmq.client.Channel;

public class SimpleMessageListenerContainerExtend extends SimpleMessageListenerContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageListenerContainer.class);

    private Environment env;

    public SimpleMessageListenerContainerExtend(Environment env) {
        this.env = env;
    }

    private boolean isGray() {
        String envflag = EnvUtil.getEnvFlag();
        if (envflag != null && !StringUtils.equalsIgnoreCase(envflag, "normal")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String[] getQueueNames() {
        String enableGray = this.env.getProperty("spring.rabbitmq.gray", "true");
        if (this.isGray() && BooleanUtils.toBoolean(enableGray)) {
            return new String[] {};
        } else {
            return super.getQueueNames();
        }
    }

    @Override
    protected void invokeListener(Channel channel, Message message) {
        try {
            String traceId = (String)message.getMessageProperties().getHeaders().get(TrackingUtils.TRACE_ID_HEAD);
            TrackingUtils.saveTrace(traceId);
            LOGGER.debug("do invokelistener and the traceId is:{}", traceId);
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            super.invokeListener(channel, message);
        } finally {
            try {
                ReceiveTraceLog traceLog = new ReceiveTraceLog(this, channel, message);
                traceLog.log();
            } catch (Throwable e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }
}
