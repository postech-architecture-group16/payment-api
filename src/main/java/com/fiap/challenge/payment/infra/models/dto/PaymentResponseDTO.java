package com.fiap.challenge.payment.infra.models.dto;

import java.util.UUID;

public record PaymentResponseDTO(UUID orderId, UUID paymentId, Boolean isPaid, Long orderNumber) {
	// Constructor, getters, and other methods can be added if needed
}
