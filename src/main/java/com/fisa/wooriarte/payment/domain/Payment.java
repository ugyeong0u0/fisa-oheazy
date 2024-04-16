package com.fisa.wooriarte.payment.domain;

import com.fisa.wooriarte.payment.dto.PaymentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Long amount;

    @Column
    private Long approvalNumber;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime CreateAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private PaymentStatus status;

    public void updatePayment(PaymentDTO paymentDTO) {
        this.approvalNumber = paymentDTO.getApprovalNumber();
        this.status = paymentDTO.getStatus();
    }
}
