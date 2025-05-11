package com.fiap.challenge.payment.infra.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Generated;

@Configuration
@Generated
public class MqSenderConfig {

	@Value("${queue.name.producer}")
	private String message;
	
	@Bean
	public Queue queue() {
		return new Queue(message, true);
	}
}
