package com.fisa.wooriarte.ticket.dto;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.user.domain.User;
import lombok.*;


@Getter
@Builder
@ToString
public class TicketDto {
    private Long ticketId;
    private Long exhibitId;
    private Long userId;
    private Long amount;
    private Boolean canceled;
    private String ticketNo;
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
                .ticketNo(this.ticketNo)
                .status(this.status)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .build();
    }

    public static TicketDto fromEntity(Ticket ticket) {
        return TicketDto.builder()
                .ticketId(ticket.getTicketId())
                .amount(ticket.getAmount())
                .canceled(ticket.getCanceled())
                .ticketNo(ticket.getTicketNo())
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

    public void setTicketNo(String ticketNo) {this.ticketNo=ticketNo;}
}
