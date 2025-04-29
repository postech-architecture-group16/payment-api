package com.fiap.challenge.payment.application.usecases;

public interface ConfirmPaymentUseCase {

	Boolean confirmPayment(String orderId, String paymentId, String paymentStatus);
}
