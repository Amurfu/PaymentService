package com.amurfu.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotBlank String concepto,
        @NotBlank String quienRealiza,
        @NotBlank String aQuienSePaga,
        @Positive BigDecimal monto
) {}
