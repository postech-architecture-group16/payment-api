package com.fiap.challenge.payment.infra.database.entities;

import java.util.UUID;

import com.fiap.challenge.payment.application.domain.models.Order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class OrderPaymentEntity {

	  private UUID id;
	  private String orderId;
	  
	  private String paymentId;
	  
	  private Long orderNumber;
	  
	  private Boolean isPaid;

	  public Order toOrder() {
		  return new Order(
				  UUID.fromString(orderId),
				  UUID.fromString(paymentId),
				  orderNumber,
				  isPaid
		  );
	  }

	@DynamoDbPartitionKey
	public UUID getId() {
		return id;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}
	  
	  
}
	  
	  
