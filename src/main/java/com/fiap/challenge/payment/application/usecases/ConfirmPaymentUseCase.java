package com.fiap.challenge.payment.application.usecases;

import com.fiap.challenge.payment.application.domain.models.Order;

public interface ConfirmPaymentUseCase {

	Order confirmPayment(String orderId);
}
