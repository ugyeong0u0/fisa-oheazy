package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDto {
    private Long projectItemId;
    private Long projectManagerId;
    private String title;
    private String artistName;
    private String intro;
    private String phone;
    private Boolean approval;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDeleted;
    private String city;
    private String itemType;
    private List<String> urls;

    public ProjectItem toEntity(ProjectManager projectManager) {

        return ProjectItem.builder()
                .projectItemId(this.projectItemId)
                .projectManager(projectManager)
                .title(this.title)
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
                .title(entity.getTitle())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .approval(entity.getApproval())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isDeleted(entity.getIsDeleted())
                .city(entity.getCity().name())
                .itemType(entity.getItemType())
                .urls(entity.getProjectPhotos().stream().map(ProjectPhoto::getUrl).collect(Collectors.toList()))
                .build();
    }
}
