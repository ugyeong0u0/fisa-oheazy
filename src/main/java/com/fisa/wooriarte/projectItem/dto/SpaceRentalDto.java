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
public class SpaceRentalDto {
    private Long projectItemId;

    private Long projectManagerId;

    private String artistName;
    private String intro;
    private String phone;
    private Boolean approval;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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
                .city(this.city)
                .build();
    }

    public static SpaceRentalDto fromEntity(ProjectItem entity) {
        return SpaceRentalDto.builder()
                .projectItemId(entity.getProjectItemId())
                .projectManagerId(entity.getProjectManager().getProjectManagerId())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .approval(entity.getApproval())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isDeleted(entity.getIsDeleted())
                .city(entity.getCity())
                .build();
    }
}
