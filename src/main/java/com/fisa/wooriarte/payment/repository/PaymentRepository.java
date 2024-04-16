package com.fisa.wooriarte.payment.repository;

import com.fisa.wooriarte.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
