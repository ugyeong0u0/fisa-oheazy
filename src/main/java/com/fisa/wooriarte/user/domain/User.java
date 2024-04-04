package com.fisa.wooriarte.user.domain;


import com.fisa.wooriarte.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name="create_at")
    private LocalDateTime createAt;

    @CreationTimestamp //현재시간을 나타내기 위한 어노테이션
    @Column(name="update_at")
    private LocalDateTime updateAt;

    @Column
    private boolean deleted;

    // Ticket 엔티티를 참조하는 필드 추가
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @Builder
    public User(int userId, @NonNull String id, @NonNull String pwd, @NonNull String name, @NonNull String email,
    @NonNull String phone, LocalDateTime createAt, LocalDateTime updateAt, boolean deleted){
        this.userId = userId;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deleted = deleted;
    }

}
