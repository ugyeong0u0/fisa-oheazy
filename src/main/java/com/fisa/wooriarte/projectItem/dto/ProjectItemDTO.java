package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDTO {

    private Long projectId;

    private Long businessId;

    private String intro;

    private String phone;

    private boolean approval;

    private LocalDateTime createdAt;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isDeleted;

    public ProjectItem toEntity() {
        return ProjectItem.builder()
                .projectId(this.projectId)
                .businessId(this.businessId)
                .intro(this.intro)
                .phone(this.phone)
                .approval(this.approval)
                .createdAt(this.createdAt)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isDeleted(this.isDeleted)
                .build();
    }
}
