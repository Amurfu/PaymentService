package com.amurfu.paymentservice.service;

import com.amurfu.paymentservice.config.PaymentStatusPublisher;
import com.amurfu.paymentservice.dto.*;
import com.amurfu.paymentservice.exception.PaymentNotFoundException;
import com.amurfu.paymentservice.model.Payment;
import com.amurfu.paymentservice.repository.PaymentRepository;
import com.amurfu.paymentservice.repository.PaymentStatusRepository;
import com.amurfu.paymentservice.utils.mapper.PaymentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repo;
    private final PaymentStatusRepository statusRepo;
    private final PaymentMapper mapper;
    private final PaymentStatusPublisher publisher;

    @Transactional
    public PaymentResponse create(PaymentRequest req) {
        var status = statusRepo.findByCode("PENDIENTE");
        Payment saved = repo.save(mapper.toEntity(req, status));
        return mapper.toResponse(saved);
    }

    public PaymentResponse find(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional
    public PaymentResponse changeStatus(Long id, String nuevoStatus) {
        var pago = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        var status = statusRepo.findByCode(nuevoStatus.toUpperCase());
        if (status == null)
            throw new IllegalArgumentException("Status inválido: " + nuevoStatus);

        pago.setStatus(status);
        pago.setUpdatedAt(java.time.Instant.now());
        publisher.publish(pago);          // notifica a Rabbit

        return mapper.toResponse(pago);
    }
}

