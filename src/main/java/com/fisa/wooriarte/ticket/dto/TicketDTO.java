package com.fisa.wooriarte.ticket.dto;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.exhibit.repository.ExhibitRepository;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Optional;


@Getter
@Builder
@ToString
public class TicketDTO {
    private Long ticketId;
    private Long exhibitId;
    private Long userId;
    private Long amount;
    private Boolean canceled;
    private String ticket_no;
    private Boolean status;
    private String name;
    private String email;
    private String phone;

    public Ticket toEntity(User user, Exhibit exhibit) {
        return Ticket.builder()
                .ticketId(this.ticketId)
                .exhibit(exhibit)
                .user(user)
                .amount(this.amount)
                .canceled(this.canceled)
                .ticket_no(this.ticket_no)
                .status(this.status)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .build();
    }

    public static TicketDTO fromEntity(Ticket ticket) {
        return TicketDTO.builder()
                .amount(ticket.getAmount())
                .canceled(ticket.getCanceled())
                .ticket_no(ticket.getTicket_no())
                .status(ticket.getStatus())
                .name(ticket.getName())
                .email(ticket.getEmail())
                .phone(ticket.getPhone())
                .exhibitId(ticket.getExhibit() != null ? ticket.getExhibit().getExhibitId() : null)
                .userId(ticket.getUser() != null ? ticket.getUser().getUserId() : null)
                .build();
    }

    public void setUserId(Long userId) {
        this.userId=userId;
    }

    public void setExhibitId(Long exhibitId) {
        this.exhibitId=exhibitId;
    }
}
