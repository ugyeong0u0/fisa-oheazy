package com.fisa.wooriarte.payment.domain;

import com.fisa.wooriarte.ticket.domain.Ticket;
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

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Ticket ticket;

    @Column(nullable = false, updatable = false, unique = true)
    private String orderNumber;

    @Column
    private String method;

    @Column(nullable = false, updatable = false)
    private Long amount;

    @Column
    private String approvalNumber;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime CreateAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private PaymentStatus status;

    public void updatePayment(String method, String approvalNumber, PaymentStatus status) {
        this.method = method;
        this.approvalNumber = approvalNumber;
        this.status = status;
    }

    public void setStatus(PaymentStatus status) {this.status = status;}
}
