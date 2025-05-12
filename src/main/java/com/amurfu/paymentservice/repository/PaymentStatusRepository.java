package com.amurfu.paymentservice.repository;

import com.amurfu.paymentservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Byte> {

    PaymentStatus findByCode(String code);

}