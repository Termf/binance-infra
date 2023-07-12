
package com.binance.platform.rabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.binance.platform.rabbit.extend.SimpleMessageListenerContainerExtend;

@Configuration
@ConditionalOnClass(EnableRabbit.class)
class RabbitAnnotationDrivenConfiguration {

	@EnableRabbit
	@ConditionalOnMissingBean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	protected static class EnableRabbitConfiguration {

	}

	protected static class SimpleRabbitListenerContainerFactoryExtend extends SimpleRabbitListenerContainerFactory {

		private final Environment env;

		public SimpleRabbitListenerContainerFactoryExtend(Environment env) {
			this.env = env;
		}

		@Override
		protected SimpleMessageListenerContainer createContainerInstance() {
			return new SimpleMessageListenerContainerExtend(env);
		}
	}

	private final ObjectProvider<MessageConverter> messageConverter;

	private final ObjectProvider<MessageRecoverer> messageRecoverer;

	private final RabbitProperties properties;

	RabbitAnnotationDrivenConfiguration(ObjectProvider<MessageConverter> messageConverter,
			ObjectProvider<MessageRecoverer> messageRecoverer, RabbitProperties properties) {
		this.messageConverter = messageConverter;
		this.messageRecoverer = messageRecoverer;
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory,
			Environment env) {
		SimpleRabbitListenerContainerFactoryExtend factory = new SimpleRabbitListenerContainerFactoryExtend(env);
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	@ConditionalOnMissingBean
	public SimpleRabbitListenerContainerFactoryConfigurer rabbitListenerContainerFactoryConfigurer() {
		SimpleRabbitListenerContainerFactoryConfigurer configurer = new SimpleRabbitListenerContainerFactoryConfigurer();
		configurer.setMessageConverter(this.messageConverter.getIfUnique());
		configurer.setMessageRecoverer(this.messageRecoverer.getIfUnique());
		configurer.setRabbitProperties(this.properties);
		return configurer;
	}

}
