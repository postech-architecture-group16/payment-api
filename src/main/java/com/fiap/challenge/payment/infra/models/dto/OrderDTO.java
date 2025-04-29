package com.fiap.challenge.payment.infra.models.dto;

import java.util.UUID;


public record OrderDTO(UUID id, 
		Long orderNumber, 
		String paymentId,
		Boolean isPaid) {
}
