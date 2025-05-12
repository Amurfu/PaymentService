package com.amurfu.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiResponse<T>(
        String statusCode,
        String message,
        Integer codeResponse,
        T payload
) {}
