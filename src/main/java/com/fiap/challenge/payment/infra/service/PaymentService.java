package com.fiap.challenge.payment.infra.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.application.usecases.ConfirmPaymentUseCase;
import com.fiap.challenge.payment.application.usecases.CreatePaymentUseCase;
import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;
import com.fiap.challenge.payment.infra.database.repositories.OrderPaymentRepository;

@Service
public class PaymentService implements CreatePaymentUseCase, ConfirmPaymentUseCase {

	private OrderPaymentRepository orderPaymentRepository;
	
	public PaymentService(OrderPaymentRepository orderPaymentRepository) {
		this.orderPaymentRepository = orderPaymentRepository;
	}

	@Override
	public Order createPayment(Order order) {
		OrderPaymentEntity orderPayment = new OrderPaymentEntity();
		orderPayment.setId(UUID.randomUUID());
		orderPayment.setOrderId(String.valueOf(order.getId()));
		orderPayment.setPaymentId(String.valueOf(UUID.randomUUID()));
		orderPayment.setOrderNumber(order.getOrderNumber());
		orderPayment.setIsPaid(Boolean.FALSE);
		
		return orderPaymentRepository.save(orderPayment).toOrder();
	}

	@Override
	public Order confirmPayment(String orderId) {
		OrderPaymentEntity orderEntity = orderPaymentRepository.findByOrderId(orderId);
			orderEntity.setIsPaid(Boolean.TRUE);
			return orderPaymentRepository.save(orderEntity).toOrder();
	}

}
