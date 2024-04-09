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
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private Boolean deleted;

    public ProjectManager toEntity() {
        return ProjectManager.builder()
                .projectManagerId(this.projectManagerId)
                .businessNumber(this.businessNumber)
                .id(this.id)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .deleted(this.deleted)
                .build();
    }

    public static ProjectManagerDTO fromEntity(ProjectManager projectManager) {
        if (projectManager == null) {
            return null;
        }

        ProjectManagerDTO dto = new ProjectManagerDTO();
        dto.setProjectManagerId(projectManager.getProjectManagerId());
        dto.setBusinessNumber(projectManager.getBusinessNumber());
        dto.setId(projectManager.getId());
        dto.setCompany(projectManager.getCompany());
        dto.setCeo(projectManager.getCeo());
        dto.setEmail(projectManager.getEmail());
        dto.setPhone(projectManager.getPhone());
        dto.setDeleted(projectManager.getDeleted());

        return dto;
    }
}
