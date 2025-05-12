package com.amurfu.paymentservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Payment API", version = "v1",
        description = "Gestión de pagos y estatus"))
public class OpenApiConfig {}
