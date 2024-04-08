package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDTO {

    private Long projectId;

    private Long businessId;

    private String artistName;

    private String intro;

    private String phone;

    private boolean approved;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean deleted;

    public ProjectItem toEntity() {
        return ProjectItem.builder()
                .projectId(this.projectId)
                .businessId(this.businessId)
                .artistName(this.artistName)
                .intro(this.intro)
                .phone(this.phone)
                .approved(this.approved)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .deleted(this.deleted)
                .build();
    }

    public static ProjectItemDTO fromEntity(ProjectItem entity) {
        return ProjectItemDTO.builder()
                .projectId(entity.getProjectId())
                .businessId(entity.getBusinessId())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .approved(entity.isApproved())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .deleted(entity.isDeleted())
                .build();

    }
}
