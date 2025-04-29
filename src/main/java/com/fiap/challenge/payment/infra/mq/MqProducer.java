package com.fiap.challenge.payment.infra.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.challenge.payment.infra.models.dto.PaymentResponseDTO;

@Component
public class MqProducer {

    private final Queue queue;

	private RabbitTemplate rabbitTemplate;
	
	private ObjectMapper objectMapper;

	public MqProducer(@Autowired RabbitTemplate rabbitTemplate, Queue queue) {
		this.rabbitTemplate = rabbitTemplate;
		this.queue = queue;
	}
	
	public void send(PaymentResponseDTO paymentResponse) throws JsonProcessingException, AmqpException {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
		rabbitTemplate.convertAndSend(this.queue.getName(), writer.writeValueAsString(paymentResponse));
	}
	
	
}
