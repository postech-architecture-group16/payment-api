package com.fiap.challenge.payment.infra.mq;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiap.challenge.payment.infra.models.dto.PaymentResponseDTO;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@ExtendWith(MockitoExtension.class)
class MqProducerTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private Queue queue;

    @InjectMocks
    private MqProducer mqProducer;

    private PaymentResponseDTO paymentResponse;

    private UUID orderId;
    private UUID paymentId;
    
    @BeforeEach
    void setUp() {
    	orderId = UUID.randomUUID();
    	paymentId = UUID.randomUUID();
        paymentResponse = new PaymentResponseDTO(
        		orderId,
        		paymentId,
            true,
            789L
        );

    }

    @Test
    void sendShouldCallRabbitTemplateWithCorrectArguments() throws JsonProcessingException {
    	when(queue.getName()).thenReturn("test-queue");
        mqProducer.send(paymentResponse);

        verify(sqsTemplate, times(1)).send(eq("test-queue"), anyString());
    }

    @Test
    void sendShouldThrowJsonProcessingExceptionWhenSerializationFails() {
        MqProducer faultyProducer = new MqProducer(sqsTemplate, queue) {
            @Override
            public void send(PaymentResponseDTO paymentResponse) throws JsonProcessingException {
                throw new JsonProcessingException("Serialization error") {};
            }
        };

        Assertions.assertThrows(JsonProcessingException.class, () -> faultyProducer.send(paymentResponse));
    }

    @Test
    void sendShouldThrowAmqpExceptionWhenRabbitTemplateFails() {
        doThrow(new AmqpException("AMQP error")).when(sqsTemplate).send(anyString(), anyString());
        
        when(queue.getName()).thenReturn("test-queue");

        Assertions.assertThrows(AmqpException.class, () -> mqProducer.send(paymentResponse));
    }
}