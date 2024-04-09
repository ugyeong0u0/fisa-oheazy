package com.fisa.wooriarte.projectmanager.DTO;

import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
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
        return ProjectManagerDTO.builder()
                .projectManagerId(projectManager.getProjectManagerId())
                .businessNumber(projectManager.getBusinessNumber())
                .id(projectManager.getId())
                .company(projectManager.getCompany())
                .ceo(projectManager.getCeo())
                .email(projectManager.getEmail())
                .phone(projectManager.getPhone())
                .deleted(projectManager.getDeleted())
                .build();
    }
}
