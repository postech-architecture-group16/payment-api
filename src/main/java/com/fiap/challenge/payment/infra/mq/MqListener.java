package com.fiap.challenge.payment.infra.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.infra.models.dto.OrderDTO;
import com.fiap.challenge.payment.infra.service.PaymentService;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MqListener {

	private PaymentService paymentService;
	
	private ObjectMapper objectMapper;
	
	public MqListener(@Autowired PaymentService paymentService) {
		this.paymentService = paymentService;
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@SqsListener("${queue.name.listener}")
	public void receive(@Payload String message) throws JsonMappingException, JsonProcessingException {
		OrderDTO orderDTO = objectMapper.readValue(message, OrderDTO.class);
		Order order = new Order(orderDTO.id(), 
				null,
				orderDTO.orderNumber(),
				Boolean.FALSE);
		paymentService.createPayment(order);
		log.info("Order received: {}", order);
	}
	
	
}
