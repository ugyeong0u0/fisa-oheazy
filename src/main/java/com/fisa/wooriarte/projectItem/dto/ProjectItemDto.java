package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDto {
    private Long projectItemId;

    private Long projectManagerId;

    private String artistName;
    private String intro;
    private String phone;
    private Boolean approval;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDeleted;
    private String city;

    public ProjectItem toEntity(ProjectManager projectManager) {

        return ProjectItem.builder()
                .projectItemId(this.projectItemId)
                .projectManager(projectManager)
                .artistName(this.artistName)
                .intro(this.intro)
                .phone(this.phone)
                .approval(this.approval)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isDeleted(this.isDeleted)
                .city(City.valueOf(this.city))
                .build();
    }

    public static ProjectItemDto fromEntity(ProjectItem entity) {
        return ProjectItemDto.builder()
                .projectItemId(entity.getProjectItemId())
                .projectManagerId(entity.getProjectManager().getProjectManagerId())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .approval(entity.getApproval())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isDeleted(entity.getIsDeleted())
                .city(entity.getCity().name())
                .build();
    }
}
