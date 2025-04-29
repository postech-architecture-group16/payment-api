package com.fiap.challenge.payment.infra.mq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.payment.application.domain.models.Order;
import com.fiap.challenge.payment.infra.models.dto.OrderDTO;
import com.fiap.challenge.payment.infra.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class MqListenerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private MqListener mqListener;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void receiveShouldCallPaymentServiceWithCorrectOrder() throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO(
            UUID.randomUUID(),
            123L,
            UUID.randomUUID().toString(),
            Boolean.TRUE
            
        );
        String message = objectMapper.writeValueAsString(orderDTO);

        mqListener.receive(message);

        verify(paymentService, times(1)).createPayment(any(Order.class));
    }

    @Test
    void receiveShouldThrowJsonProcessingExceptionForInvalidMessage() {
        String invalidMessage = "invalid-json";

        Assertions.assertThrows(
            JsonProcessingException.class,
            () -> mqListener.receive(invalidMessage)
        );

        verify(paymentService, never()).createPayment(any(Order.class));
    }
}