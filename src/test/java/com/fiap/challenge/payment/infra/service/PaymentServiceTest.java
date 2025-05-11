package com.fiap.challenge.payment.infra.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.infra.database.entities.OrderPaymentEntity;
import com.fiap.challenge.payment.infra.database.repositories.OrderPaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderPaymentRepository orderPaymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    
    private UUID orderId;
    private UUID paymentId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        paymentId = UUID.randomUUID();
        order = new Order(
                orderId,
                paymentId,
                2L,
                true
            );
    }

    @Test
    void createPaymentShouldSaveAndReturnOrder() {
        // Arrange
        OrderPaymentEntity savedEntity = new OrderPaymentEntity();
        savedEntity.setOrderId(String.valueOf(order.getId()));
        savedEntity.setOrderNumber(order.getOrderNumber());
        savedEntity.setPaymentId(paymentId.toString());
        savedEntity.setIsPaid(false);

        when(orderPaymentRepository.save(any(OrderPaymentEntity.class))).thenReturn(savedEntity);

        // Act
        Order result = paymentService.createPayment(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderNumber(), result.getOrderNumber());
        assertEquals(false, result.getIsPaid());

        verify(orderPaymentRepository, times(1)).save(any(OrderPaymentEntity.class));
    }

    @Test
    void confirmPaymentShouldUpdateAndReturnOrder() {
        // Arrange
        String orderId = String.valueOf(order.getId());
        OrderPaymentEntity existingEntity = new OrderPaymentEntity();
        existingEntity.setOrderId(orderId);
        existingEntity.setOrderNumber(order.getOrderNumber());
        existingEntity.setPaymentId(paymentId.toString());
        existingEntity.setIsPaid(false);

        when(orderPaymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(existingEntity));
        when(orderPaymentRepository.save(existingEntity)).thenReturn(existingEntity);

        // Act
        Order result = paymentService.confirmPayment(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderNumber(), result.getOrderNumber());
        assertEquals(true, result.getIsPaid());

        verify(orderPaymentRepository, times(1)).findByOrderId(orderId);
        verify(orderPaymentRepository, times(1)).save(existingEntity);
    }

}
