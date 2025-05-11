package com.fiap.challenge.payment.infra.database.repositories;

import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;


public interface OrderPaymentRepository{
	
	OrderPaymentEntity save(OrderPaymentEntity orderPaymentEntity);
	OrderPaymentEntity findByOrderId(String orderId);
	OrderPaymentEntity findByPaymentId(String paymentId);

}
