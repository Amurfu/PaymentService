package controller;


import com.amurfu.paymentservice.controller.GlobalExceptionHandler;
import com.amurfu.paymentservice.dto.ApiResponse;
import com.amurfu.paymentservice.exception.PaymentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundDevuelve404() {
        ResponseEntity<ApiResponse<Void>> resp =
                handler.handleNotFound(new PaymentNotFoundException(50L));

        assertThat(resp.getStatusCode().value()).isEqualTo(404);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().statusCode()).isEqualTo("ERROR");
    }
}