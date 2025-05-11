package com.fiap.challenge.payment.infra.database.repositories.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;
import com.fiap.challenge.payment.infra.database.repositories.OrderPaymentRepository;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class OrderPaymentRepositoryImpl implements OrderPaymentRepository {

	private final DynamoDbTemplate dynamoDbTemplate;
	
	public OrderPaymentRepositoryImpl(DynamoDbTemplate dynamoDbTemplate) {
		this.dynamoDbTemplate = dynamoDbTemplate;
	}

	@Override
	public OrderPaymentEntity save(OrderPaymentEntity orderPaymentEntity) {
		dynamoDbTemplate.save(orderPaymentEntity);
		return orderPaymentEntity;
	}

	@Override
	public Optional<OrderPaymentEntity> findByOrderId(String orderId) {
		ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
				.filterExpression(Expression.builder()
					.expression("orderId = :orderIdVal")
					.expressionValues(Map.of(":orderIdVal", AttributeValue.builder().s(orderId).build()))
					.build())
				.build();
		
		var history = dynamoDbTemplate.scan(scanEnhancedRequest, OrderPaymentEntity.class);

		return history.items().stream().findFirst();
	}

	@Override
	public Optional<OrderPaymentEntity> findByPaymentId(String paymentId) {
		ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
				.filterExpression(Expression.builder()
					.expression("paymentId = :paymentIdVal")
					.expressionValues(Map.of(":paymentId", AttributeValue.builder().s(paymentId).build()))
					.build())
				.build();
		
		var history = dynamoDbTemplate.scan(scanEnhancedRequest, OrderPaymentEntity.class);
		return history.items().stream().findFirst();
	}

}
