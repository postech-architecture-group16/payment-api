package com.fiap.challenge.payment.infra.database.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fiap.challenge.payment.application.domain.models.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("order_payments")
@Getter
@Setter
@NoArgsConstructor
public class OrderPaymentEntity {

	  @Id
	  private String id;
	  @Field("order_id")
	  private String orderId;
	  
	  @Field("payment_id")
	  private String paymentId;
	  
	  @Field("order_number")
	  private Long orderNumber;
	  
	  @Field("is_paid")
	  private Boolean isPaid;

	  public Order toOrder() {
		  return new Order(
				  UUID.fromString(orderId),
				  null,
				  null,
				  UUID.fromString(paymentId),
				  orderNumber,
				  null,
				  null,
				  isPaid
		  );
	  }
}
	  
	  
