package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectItemResponseDto {
    private Long projectItemId;
    private Long projectManagerId;
    private String artistName;
    private String intro;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String city;
    private String itemType;
    private String url;

    public static ProjectItemResponseDto fromEntity(ProjectItem entity) {
        return ProjectItemResponseDto.builder()
                .projectItemId(entity.getProjectItemId())
                .projectManagerId(entity.getProjectManager().getProjectManagerId())
                .artistName(entity.getArtistName())
                .intro(entity.getIntro())
                .phone(entity.getPhone())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .city(entity.getCity().name())
                .itemType(entity.getItemType())
                .url(entity.getProjectPhotos().get(0).getUrl())
                .build();
    }
}
