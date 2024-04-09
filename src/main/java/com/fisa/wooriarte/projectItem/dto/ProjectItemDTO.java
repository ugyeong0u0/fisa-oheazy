package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDTO {

    private Long projectItemId;

    private ProjectManager projectManager;

    private String artistName;

    private String intro;

    private String phone;

    private Boolean approval;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isDeleted;

    public ProjectItem toEntity() {
        return ProjectItem.builder()
                .projectItemId(this.projectItemId)
                .projectManager(this.projectManager)
                .artistName(this.artistName)
                .intro(this.intro)
                .phone(this.phone)
                .approval(this.approval)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isDeleted(this.isDeleted)
                .build();
    }

    public static ProjectItemDTO fromEntity(ProjectItem entity) {
        return ProjectItemDTO.builder()
                .projectItemId(entity.getProjectItemId())
                .projectManager(entity.getProjectManager())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .approval(entity.getApproval())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isDeleted(entity.getIsDeleted())
                .build();

    }
}
