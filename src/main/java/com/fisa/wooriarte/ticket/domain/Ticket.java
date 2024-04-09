package com.fisa.wooriarte.ticket.domain;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.user.domain.User;
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
    private Long ticketId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Exhibit exhibitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User userId;

    @Column(nullable = false)
    private Long amount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean canceled;

    @Column(nullable = false)
    private String ticket_no;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;


    // PrePersist 메서드: 엔티티가 영구 저장되기 전에 자동으로 호출됨
    @PrePersist
    public void generateTicketNo() {
        StringBuilder ticketNoBuilder = new StringBuilder();
        ticketNoBuilder.append(ticketId).append("-");
        if (exhibitId != null) {
            ticketNoBuilder.append(exhibitId).append("-");
        }
        if (userId != null) {
            ticketNoBuilder.append(userId).append("-");
        }
        ticketNoBuilder.append(date.toString());

        this.ticket_no = ticketNoBuilder.toString();
    }

    //티켓 취소 canceled 컬럼 변경
    public void setCanceled() {
        this.canceled = true;
    }

    //티켓 상태 status 컬럼 변경
    public void setstatus() {
        this.status = !this.status;
    }
}
