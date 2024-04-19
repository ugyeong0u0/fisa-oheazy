package com.fisa.wooriarte.payment.repository;

import com.fisa.wooriarte.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderNumber(String orderNumber);

    Optional<Payment> findByApprovalNumber(String approvalNumber);
}
