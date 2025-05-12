package com.amurfu.paymentservice.controller;


import com.amurfu.paymentservice.dto.ApiResponse;
import com.amurfu.paymentservice.dto.PaymentRequest;
import com.amurfu.paymentservice.dto.PaymentResponse;
import com.amurfu.paymentservice.dto.StatusChangeRequest;
import com.amurfu.paymentservice.service.PaymentService;
import com.amurfu.paymentservice.utils.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private  PaymentService service;

    @Operation(summary = "Crear pago")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest req) {
        var resp = service.create(req);
        return ApiResponseUtil.success(resp, "Pago creado", HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar pago por ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> get(@PathVariable Long id) {
        var resp = service.find(id);
        return ApiResponseUtil.success(resp, "Consulta exitosa", HttpStatus.OK);
    }

    @Operation(summary = "Cambiar estatus y notificar")
    @PatchMapping("{id}/status")
    public ResponseEntity<ApiResponse<PaymentResponse>> change(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest body) {
        var resp = service.changeStatus(id, body.nuevoStatus());
        return ApiResponseUtil.success(resp, "Estatus actualizado", HttpStatus.OK);
    }
}