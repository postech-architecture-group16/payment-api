package com.fiap.challenge.payment.infra.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;

public interface OrderPaymentRepository extends MongoRepository<OrderPaymentEntity, String> {
	OrderPaymentEntity findByOrderId(String orderId);
	OrderPaymentEntity findByPaymentId(String paymentId);

}
