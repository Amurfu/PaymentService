package com.amurfu.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        String concepto,
        String quienRealiza,
        String aQuienSePaga,
        BigDecimal monto,
        String status
) {}
