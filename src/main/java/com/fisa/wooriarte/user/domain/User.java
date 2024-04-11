package com.fisa.wooriarte.user.domain;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long userId;

    @NonNull
    @Column
    private String id;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

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
    @Column
    private LocalDateTime createAt;

    @CreatedDate //현재시간을 나타내기 위한 어노테이션
    @Column
    private LocalDateTime updateAt;

    @Column
    private Boolean deleted;




    public void setDeleted(){
        this.deleted = !this.deleted;
    }


}
