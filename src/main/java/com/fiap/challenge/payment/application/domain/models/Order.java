package com.fiap.challenge.payment.application.domain.models;

import java.util.UUID;

public class Order {

	private UUID id;
	
	private Long orderNumber;
	
	private UUID paymentId;
	
	private Boolean isPaid;

	public Order(UUID id, 
			UUID paymentId, 
			Long orderNumber,
			Boolean isPaid) {
		this.id = id;
		this.paymentId = paymentId;
		this.orderNumber = orderNumber;
		this.isPaid = isPaid;
	}
	
	public UUID getId() {
		return id;
	}

	public UUID getPaymentId() {
		return paymentId;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	
	

}
