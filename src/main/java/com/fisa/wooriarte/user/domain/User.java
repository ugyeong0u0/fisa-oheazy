package com.fisa.wooriarte.user.domain;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long userId;

    @NonNull
    @Column(length = 20)
    private String id;

    @NonNull
    @Column
    private String pwd;

    @NonNull
    @Column
    private String name;

    @NonNull
    @Column
    private String email;

    @NonNull
    @Column
    private String phone;

    @CreationTimestamp //현재시간을 나타내기 위한 어노테이션
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @CreatedDate //현재시간을 나타내기 위한 어노테이션
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column
    private boolean deleted;

    // Ticket 엔티티를 참조하는 필드 추가
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public UserDTO toDto() {
        return UserDTO.builder()
                .userId(this.userId)
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .createAt(this.createAt)
                .updateAt(this.updateAt)
                .deleted(this.deleted)
                .build();
    }



}
