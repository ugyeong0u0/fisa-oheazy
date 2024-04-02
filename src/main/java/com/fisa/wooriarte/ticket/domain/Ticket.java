package com.fisa.wooriarte.ticket.domain;

import com.fisa.wooriarte.ticket.dto.TicketDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
@DynamicUpdate

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticket_id;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "exhbt_id", nullable = false)
//    private Exhibit exhibit;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @Column(nullable = false)
    private int amount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private boolean canceled;

    @Column(nullable = false)
    private String ticket_no;

    @Column(nullable = false)
    private boolean status;

    public void generateTicketNo() {
        StringBuilder ticketNoBuilder = new StringBuilder();
        ticketNoBuilder.append(ticket_id).append("-");
//        if (exhibit != null) {
//            ticketNoBuilder.append(exhibit.getId()).append("-");
//        }
//        if (user != null) {
//            ticketNoBuilder.append(user.getId()).append("-");
//        }
        ticketNoBuilder.append(date.toString());

        this.ticket_no = ticketNoBuilder.toString();
    }
}
