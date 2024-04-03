package com.fisa.wooriarte.ticket.dto;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TicketDTO {
    private long ticketId;
//    private long exhbtId;
    private long user;
    private int amount;
    private LocalDateTime date;
    private boolean canceled;
    private String ticket_no;
    private boolean status;

    @Builder
    public Ticket toEntity(UserRepository userRepository) {
        // 사용자를 찾아서 Optional로 받음
        Optional<User> optionalUser = userRepository.findById(Math.toIntExact(this.user));

        // Optional에서 사용자를 가져오거나 사용자가 존재하지 않으면 예외 발생
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + this.user));

        return Ticket.builder()
                .ticketId(this.ticketId)
                .user(user)
                .amount(this.amount)
                .date(this.date)
                .canceled(this.canceled)
                .ticket_no(this.ticket_no)
                .status(this.status)
                .build();
    }

    public static TicketDTO fromEntity(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setAmount(ticket.getAmount());
        dto.setDate(ticket.getDate());
        dto.setCanceled(ticket.isCanceled());
        dto.setTicket_no(ticket.getTicket_no());
        dto.setStatus(ticket.isStatus());

//        if (ticket.getExhibit() != null) {
//            dto.setExhibit_id(ticket.getExhibit().getId());
//        }
        if (ticket.getUser() != null) {
            dto.setUser(ticket.getUser().getUserId()); // User 엔티티 대신 userId를 사용
        }

        return dto;
    }
}
