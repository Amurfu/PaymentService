package com.amurfu.paymentservice.service;


import com.amurfu.paymentservice.config.PaymentStatusPublisher;
import com.amurfu.paymentservice.dto.*;
import com.amurfu.paymentservice.exception.PaymentNotFoundException;
import com.amurfu.paymentservice.model.*;
import com.amurfu.paymentservice.repository.*;
import com.amurfu.paymentservice.utils.mapper.PaymentMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository repo;
    @Mock
    PaymentStatusRepository statusRepo;
    @Mock
    PaymentMapper mapper;
    @Mock
    PaymentStatusPublisher publisher;

    @InjectMocks PaymentService service;   // SUT

    // Datos de prueba reusables
    private final PaymentStatus pending = new PaymentStatus();
    private final PaymentStatus paid    = new PaymentStatus();
    private final PaymentRequest req = new PaymentRequest(
            "Servicio", "Ana", "Proveedor", BigDecimal.valueOf(100));

    @BeforeEach
    void initStatuses() {
        pending.setId((byte) 1); pending.setCode("PENDIENTE");
        paid.setId((byte) 2);    paid.setCode("PAGADO");
    }

    /* ---------------------------------------------------------------------- */
    @Test
    void crearPago_guarda_yDevuelveResponse() {
        Payment entity = new Payment(); entity.setStatus(pending);
        PaymentResponse dtoResp = new PaymentResponse(1L,"Servicio","Ana",
                "Proveedor", BigDecimal.valueOf(100),"PENDIENTE");

        when(statusRepo.findByCode("PENDIENTE")).thenReturn(pending);
        when(mapper.toEntity(req, pending)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(dtoResp);

        PaymentResponse out = service.create(req);

        assertThat(out).isEqualTo(dtoResp);
        verify(repo).save(entity);
        verify(publisher, never()).publish(any());
    }

    /* ---------------------------------------------------------------------- */
    @Test
    void find_retornaPagoCuandoExiste() {
        Payment entity = new Payment(); entity.setStatus(pending);
        PaymentResponse dto = new PaymentResponse(7L,"Serv","Ana","Prov",
                BigDecimal.TEN,"PENDIENTE");

        when(repo.findById(7L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(dto);

        PaymentResponse result = service.find(7L);

        assertThat(result.status()).isEqualTo("PENDIENTE");
    }

    @Test
    void find_lanzaExcepcionCuandoNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.find(99L))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    /* ---------------------------------------------------------------------- */
    @Test
    void changeStatus_actualizaYPublica() {
        Payment entity = new Payment(); entity.setId(1L); entity.setStatus(pending);

        when(repo.findById(1L)).thenReturn(Optional.of(entity));
        when(statusRepo.findByCode("PAGADO")).thenReturn(paid);
        when(mapper.toResponse(entity))
                .thenReturn(new PaymentResponse(1L,"s","q","p",BigDecimal.TEN,"PAGADO"));

        PaymentResponse result = service.changeStatus(1L, "PAGADO");

        assertThat(result.status()).isEqualTo("PAGADO");
        verify(publisher).publish(entity);
    }

    @Test
    void changeStatus_statusInvalido_lanzaExcepcion() {
        Payment entity = new Payment(); entity.setStatus(pending);
        when(repo.findById(1L)).thenReturn(Optional.of(entity));
        when(statusRepo.findByCode("NOEXISTE")).thenReturn(null);

        assertThatThrownBy(() -> service.changeStatus(1L, "NOEXISTE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Status inválido");
    }
}
