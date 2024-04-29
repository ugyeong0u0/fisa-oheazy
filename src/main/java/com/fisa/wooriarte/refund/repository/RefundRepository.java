package com.fisa.wooriarte.refund.repository;

import com.fisa.wooriarte.refund.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
