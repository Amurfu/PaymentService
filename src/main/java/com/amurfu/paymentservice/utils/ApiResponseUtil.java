package com.amurfu.paymentservice.utils;


import com.amurfu.paymentservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResponseUtil {

    private ApiResponseUtil() {}

    public static <T> ResponseEntity<ApiResponse<T>> success(T body, String msg, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(ApiResponse.<T>builder()
                        .statusCode("OK")
                        .message(msg)
                        .codeResponse(code.value())
                        .payload(body)
                        .build());
    }

    public static ResponseEntity<ApiResponse<Void>> error(String msg, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(ApiResponse.<Void>builder()
                        .statusCode("ERROR")
                        .message(msg)
                        .codeResponse(code.value())
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            T body, String msg, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(ApiResponse.<T>builder()
                        .statusCode("ERROR")
                        .message(msg)
                        .codeResponse(code.value())
                        .payload(body)
                        .build());
    }
}
