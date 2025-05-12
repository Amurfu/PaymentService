package com.amurfu.paymentservice.config;


import com.amurfu.paymentservice.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
class RabbitConfig {

    public static final String EXCHANGE = "payment-status.x";
    public static final String QUEUE    = "payment-status.q";
    private static final String ROUTING_KEY = "status";

    @Bean
    DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean Queue queue() { return QueueBuilder.durable(QUEUE).build(); }

    @Bean Binding bind() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
    }
}
