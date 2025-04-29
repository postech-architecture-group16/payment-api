package com.fiap.challenge.payment.infra.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.challenge.payment.application.domain.models.Customer;
import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.application.domain.models.OrderProduct;


public record OrderDTO(UUID id, 
		Customer customer, 
		BigDecimal total, 
		Long orderNumber, 
		LocalDateTime createAt, 
		LocalDateTime updateAt,
		List<OrderProduct> products,
		String paymentId,
		Boolean isPaid) {
	
	public OrderDTO(Order order) {
		this(
				order.getId(), 
				order.getCustomer(), 
				order.getTotal(), 
				order.getOrderNumber(), 
				order.getCreateAt(), 
				order.getUpdateAt(), 
				order.getProducts(), null, null);
	}

}
