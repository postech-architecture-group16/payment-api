package com.fiap.challenge.payment.infra.database.repositories;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;
import com.fiap.challenge.payment.infra.database.repositories.impl.OrderPaymentRepositoryImpl;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@ExtendWith(MockitoExtension.class)
class OrderPaymentRepositoryImplTest {

	@InjectMocks
	private OrderPaymentRepositoryImpl orderPaymentRepository;

	@Mock
	private DynamoDbTemplate dynamoDbTemplate;
	
	private OrderPaymentEntity orderPaymentEntity;
	
	@BeforeEach
	void setUp() {
		orderPaymentEntity = new OrderPaymentEntity();
		orderPaymentEntity.setId(UUID.randomUUID());
		orderPaymentEntity.setOrderId("orderId");
		orderPaymentEntity.setPaymentId("paymentId");
		orderPaymentEntity.setOrderNumber(123L);
		orderPaymentEntity.setIsPaid(false);
	}
	
	@Test
	void shouldSaveOrderPaymentEntity() {
		
		when(dynamoDbTemplate.save(any(OrderPaymentEntity.class))).thenReturn(orderPaymentEntity);
		OrderPaymentEntity result = orderPaymentRepository.save(orderPaymentEntity);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(orderPaymentEntity, result);
		verify(dynamoDbTemplate).save(orderPaymentEntity);
	}
	
	@Test
	void shouldFindByOrderId() {
		String orderId = orderPaymentEntity.getOrderId();
		
		var page = Page. create(List.of(orderPaymentEntity));
		SdkIterable<Page<OrderPaymentEntity>> paymentPage = () -> List.of(page).iterator();
		var paymentPageIterable = PageIterable.create(paymentPage);
		
		when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(OrderPaymentEntity.class))).thenReturn(paymentPageIterable);
		
		OrderPaymentEntity result = orderPaymentRepository.findByOrderId(orderId).get();
		
		Assertions.assertEquals(orderId, result.getOrderId());
		verify(dynamoDbTemplate).scan(any(ScanEnhancedRequest.class), eq(OrderPaymentEntity.class));
	}
	
	@Test
	void shouldFindByPaymentId() {
		String paymentId = orderPaymentEntity.getPaymentId();
		
		var page = Page.create(List.of(orderPaymentEntity));
		SdkIterable<Page<OrderPaymentEntity>> paymentPage = () -> List.of(page).iterator();
		var paymentPageIterable = PageIterable.create(paymentPage);
		
		when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(OrderPaymentEntity.class))).thenReturn(paymentPageIterable);
		
		OrderPaymentEntity result = orderPaymentRepository.findByPaymentId(paymentId).get();
		
		Assertions.assertEquals(paymentId, result.getPaymentId());
		verify(dynamoDbTemplate).scan(any(ScanEnhancedRequest.class), eq(OrderPaymentEntity.class));
	}
	
	
}
