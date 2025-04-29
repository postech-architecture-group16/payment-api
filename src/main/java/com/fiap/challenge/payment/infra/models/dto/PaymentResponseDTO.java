package com.fiap.challenge.payment.infra.models.dto;

import java.util.UUID;

public record PaymentResponseDTO(UUID orderId, 
		UUID paymentId, 
		Boolean isPaid, 
		Long orderNumber) {
}
