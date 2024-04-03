package com.fisa.wooriarte.projectmanager.domain;

import com.fisa.wooriarte.projectmanager.DTO.ProjectManagerDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectManagerId;

    @Column(nullable = false, unique = true)
    private long businessNumber;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private boolean deleted;

    public ProjectManagerDTO toDTO() {
        return ProjectManagerDTO.builder()
                .projectManagerId(this.projectManagerId)
                .businessNumber(this.businessNumber)
                .id(this.id)
                .pwd(this.pwd)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .createAt(this.createAt)
                .deleted(this.deleted)
                .build();
    }
}
