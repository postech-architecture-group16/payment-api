package com.fiap.challenge.payment.infra.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.infra.models.dto.PaymentResponseDTO;
import com.fiap.challenge.payment.infra.mq.MqProducer;
import com.fiap.challenge.payment.infra.service.PaymentService;

@RestController
@RequestMapping("/payment/api")
public class PaymentController {

	private PaymentService paymentService;
	private MqProducer mqProducer;
	
	public PaymentController(PaymentService paymentService, MqProducer mqProducer) {
		this.paymentService = paymentService;
		this.mqProducer = mqProducer;
	}
	
	@PatchMapping("/confirm-payment/{orderId}")
	public ResponseEntity<PaymentResponseDTO> confirmPayment(@PathVariable("orderId") String orderId) throws JsonProcessingException, AmqpException {
			Order order = paymentService.confirmPayment(orderId);
			
			PaymentResponseDTO paymentResponse = new PaymentResponseDTO(order.getId(), 
					order.getPaymentId(),  
					order.getIsPaid(), 
					order.getOrderNumber());
			
			mqProducer.send(paymentResponse);
			
		return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
	}
	
}
