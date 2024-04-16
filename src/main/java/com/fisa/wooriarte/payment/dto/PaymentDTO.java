package com.fisa.wooriarte.payment.dto;

import com.fisa.wooriarte.payment.domain.Payment;
import com.fisa.wooriarte.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long paymentId;
    private String method;
    private Long amount;
    private Long approvalNumber;
    private PaymentStatus status;

    public Payment toEntity() {
        return Payment.builder()
                .method(this.method)
                .amount(this.amount)
                .status(this.status)
                .build();
    }

    public PaymentDTO fromEntity(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .method(payment.getMethod())
                .amount(payment.getAmount())
                .approvalNumber(payment.getApprovalNumber())
                .status(payment.getStatus())
                .build();
    }
}
