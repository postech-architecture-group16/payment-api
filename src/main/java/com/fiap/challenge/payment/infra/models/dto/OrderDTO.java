package com.fiap.challenge.payment.infra.models.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDTO(UUID id, 
		Long orderNumber, 
		String paymentId,
		Boolean isPaid) {
}
