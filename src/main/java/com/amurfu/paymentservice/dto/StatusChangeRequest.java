package com.amurfu.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusChangeRequest(
        @NotBlank String nuevoStatus   // PENDIENTE, PAGADO, CANCELADO
) {}