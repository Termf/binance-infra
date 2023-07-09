//
//package com.binance.feign.sample.rabbit;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.binance.platform.rabbit.delay.Delay;
//
//@SpringBootApplication
//@EnableScheduling
//public class SampleAmqpSimpleApplication {
//
//	@Bean
//	public Sender mySender() {
//		return new Sender();
//	}
//
//	@RabbitListener(queues = "architect.queue")
//	public void process(@Payload String message) {
//
//		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		LocalDateTime now = LocalDateTime.now();
//		String time = dateTimeFormatter.format(now);
//		System.out.println(String.format("receive message***receive time===%s***%s", message, time));
//	}
//
//	public static void main(String[] args) throws Exception {
//		SpringApplication.run(SampleAmqpSimpleApplication.class, args);
//	}
//
//	static class Sender {
//
//		@Autowired
//		private RabbitTemplate rabbitTemplate;
//
//		@Autowired
//		private RabbitProperties rabbitProperties;
//
//		@Autowired
//		private com.binance.platform.rabbit.RabbitProperties properties;
//
//		@PostConstruct
//		public void pointCut() {
//			System.out.println("+++++++++++++++++++++");
//			System.out.println(rabbitProperties.getUsername());
//			System.out.println("=====================");
//			System.out.println(properties.getUsername());
//		}
//
//		@Scheduled(fixedDelay = 1000L)
//		@Delay(interval = 1000L, queue = "architect.queue")
//		public void send() {
//			String exchange = "architect.exchange";
//			String routingKey = "architect.route";
//			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			LocalDateTime now = LocalDateTime.now();
//			String message = dateTimeFormatter.format(now);
//			System.out.println(String.format("sending to exchange: %s", message));
//
//			this.rabbitTemplate.convertAndSend(exchange, routingKey, message);
//		}
//
//	}
//
//}
