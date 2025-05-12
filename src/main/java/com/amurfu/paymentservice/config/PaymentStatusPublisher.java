package com.amurfu.paymentservice.config;

import com.amurfu.paymentservice.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStatusPublisher {

    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(Payment pago) {
        try {
            rabbit.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    "status",
                    mapper.writeValueAsString(new StatusMessage(
                            pago.getId(),
                            pago.getStatus().getCode()))
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar el pago", e);
        }
    }

    private record StatusMessage(Long paymentId, String status) {}
}

