package com.fisa.wooriarte.projectmanager.DTO;

import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ProjectManagerDTO {
    private Long projectManagerId;
    private Long businessNumber;
    private String id;
    private String pwd;
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private LocalDateTime createAt;
    private Boolean deleted;

    public ProjectManager toEntity() {
        return ProjectManager.builder()
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
