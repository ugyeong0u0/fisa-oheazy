package com.fisa.wooriarte.ticket.dto;

import com.fisa.wooriarte.ticket.domain.Ticket;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TicketDTO {
    private long ticket_id;
//    private long exhbt_id;
//    private long user_id;
    private int amount;
    private LocalDateTime date;
    private boolean canceled;
    private String ticket_no;
    private boolean status;

    @Builder
    public Ticket toEntity(/* Exhbit exhbit, User user*/) {
        return Ticket.builder()
                .ticket_id(this.ticket_id)
//                .exhbt_id(this.exhbt_id)
//                .user_id(this.user_id)
                .amount(this.amount)
                .date(this.date)
                .canceled(this.canceled)
                .ticket_no(this.ticket_no)
                .status(this.status)
                .build();
    }

    public static TicketDTO fromEntity(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicket_id(ticket.getTicket_id());
        dto.setAmount(ticket.getAmount());
        dto.setDate(ticket.getDate());
        dto.setCanceled(ticket.isCanceled());
        dto.setTicket_no(ticket.getTicket_no());
        dto.setStatus(ticket.isStatus());

//        if (ticket.getExhibit() != null) {
//            dto.setExhibit_id(ticket.getExhibit().getId());
//        }
//        if (ticket.getUser() != null) {
//            dto.setUser_id(ticket.getUser().getId());
//        }

        return dto;
    }
}
