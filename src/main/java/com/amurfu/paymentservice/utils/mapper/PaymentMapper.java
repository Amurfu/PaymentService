package com.amurfu.paymentservice.utils.mapper;

import com.amurfu.paymentservice.dto.PaymentRequest;
import com.amurfu.paymentservice.dto.PaymentResponse;
import com.amurfu.paymentservice.model.Payment;
import com.amurfu.paymentservice.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest req, PaymentStatus status) {
        var entity = new Payment();
        entity.setConcepto(req.concepto());
        entity.setQuienRealiza(req.quienRealiza());
        entity.setAQuienSePaga(req.aQuienSePaga());
        entity.setMonto(req.monto());
        entity.setStatus(status);
        entity.setCreatedAt(java.time.Instant.now());
        return entity;
    }

    public PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(), p.getConcepto(), p.getQuienRealiza(),
                p.getAQuienSePaga(), p.getMonto(), p.getStatus().getCode());
    }
}