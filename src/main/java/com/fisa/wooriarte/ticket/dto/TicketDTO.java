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

    public Ticket toEntity(UserRepository userRepository, ExhibitRepository exhibitRepository) {
        // 전시를 찾아서 Optional로 받음
        Optional<Exhibit> optionalExhibit = exhibitRepository.findById(this.exhibitId);
        // 사용자를 찾아서 Optional로 받음
        Optional<User> optionalUser = userRepository.findById(this.userId);

        // Optional에서 사용자를 가져오거나 사용자가 존재하지 않으면 예외 발생
        Exhibit exhibit = optionalExhibit.orElseThrow(() -> new IllegalArgumentException("Exhibit not found with id: " + this.exhibitId));
        // Optional에서 사용자를 가져오거나 사용자가 존재하지 않으면 예외 발생
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + this.userId));

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
