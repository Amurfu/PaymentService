package com.amurfu.paymentservice.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Pago no encontrado: " + id);
    }
}
