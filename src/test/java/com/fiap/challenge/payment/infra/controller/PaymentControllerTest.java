package com.fiap.challenge.payment.infra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.infra.models.dto.PaymentResponseDTO;
import com.fiap.challenge.payment.infra.mq.MqProducer;
import com.fiap.challenge.payment.infra.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

	@Mock
    private PaymentService paymentService;

    @Mock
    private MqProducer mqProducer;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void confirmPaymentShouldReturnOkAndSendMessage() throws JsonProcessingException {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        Order order = new Order(
            orderId,
            paymentId,
            2L,
            true
        );

        when(paymentService.confirmPayment(orderId.toString())).thenReturn(order);

        // Act
        ResponseEntity<PaymentResponseDTO> response = paymentController.confirmPayment(orderId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(order.getId(), response.getBody().orderId());
        assertEquals(order.getPaymentId(), response.getBody().paymentId());
        assertEquals(order.getIsPaid(), response.getBody().isPaid());
        assertEquals(order.getOrderNumber(), response.getBody().orderNumber());

        verify(paymentService, times(1)).confirmPayment(orderId.toString());
        verify(mqProducer, times(1)).send(any(PaymentResponseDTO.class));
    }

    @Test
    void confirmPaymentShouldThrowJsonProcessingExceptionWhenMqProducerFails() throws JsonProcessingException {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        Order order = new Order(
                orderId,
                paymentId,
                2L,
                true
            );

        when(paymentService.confirmPayment(orderId.toString())).thenReturn(order);
        doThrow(JsonProcessingException.class).when(mqProducer).send(any(PaymentResponseDTO.class));

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> paymentController.confirmPayment(orderId.toString()));

        verify(paymentService, times(1)).confirmPayment(orderId.toString());
        verify(mqProducer, times(1)).send(any(PaymentResponseDTO.class));
    }
}
