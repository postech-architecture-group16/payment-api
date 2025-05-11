package com.fiap.challenge.payment.infra.database.repositories;

import java.util.Optional;

import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;


public interface OrderPaymentRepository{
	
	OrderPaymentEntity save(OrderPaymentEntity orderPaymentEntity);
	Optional<OrderPaymentEntity> findByOrderId(String orderId);
	Optional<OrderPaymentEntity> findByPaymentId(String paymentId);

}
