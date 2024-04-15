package com.fisa.wooriarte.ticket.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@DynamicUpdate

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibit_id", nullable = false)
    private Exhibit exhibit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long amount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean canceled;

    @Column(nullable = false)
    private String ticketNo;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;


//    // PrePersist 메서드: 엔티티가 영구 저장되기 전에 자동으로 호출됨
//    public void generateTicketNo() {
//        StringBuilder ticketNoBuilder = new StringBuilder();
//        ticketNoBuilder.append(ticketId).append("-");
////        if (exhibit != null) {
////            ticketNoBuilder.append(exhibit).append("-");
////        }
////        if (user != null) {
////            ticketNoBuilder.append(user).append("-");
////        }
////        ticketNoBuilder.append(date.toString());
//
//        this.ticketNo = ticketNoBuilder.toString();
//        System.out.println(ticketNo);
//    }

    //티켓 취소 canceled 컬럼 변경
    public void setCanceled() {
        this.canceled = true;
    }

    //티켓 상태 status 컬럼 변경
    public void setstatus() {
        this.status = !this.status;
    }
}
