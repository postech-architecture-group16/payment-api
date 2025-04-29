package com.fiap.challenge.payment.application.usecases;

import com.fiap.challenge.payment.application.domain.models.Order;

public interface CreatePaymentUseCase {

	Order createPayment(Order order);
}
