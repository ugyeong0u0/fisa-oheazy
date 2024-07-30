package com.fisa.wooriarte.refund.dto;

import com.fisa.wooriarte.refund.domain.Refund;
import com.fisa.wooriarte.ticket.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDto {
    private Long refundId;
    private Long ticketId;
    private Long amount;
    private LocalDateTime refundAt;
    private String reason;

    public static RefundDto fromEntity(Refund refund) {
        return RefundDto.builder()
                .refundId(refund.getRefundId())
                .ticketId(refund.getTicket().getTicketId())
                .amount(refund.getAmount())
                .refundAt(refund.getRefundAt())
                .reason(refund.getReason())
                .build();
    }
}
