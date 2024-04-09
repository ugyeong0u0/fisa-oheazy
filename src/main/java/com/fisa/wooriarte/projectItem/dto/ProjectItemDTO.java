package com.fisa.wooriarte.projectItem.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectItemDTO {
    private Long projectItemId;
<<<<<<< HEAD
    private Long projectManagerId;
=======

    private Long projectManagerId;

>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
    private String artistName;
    private String intro;
    private String phone;
    private Boolean approval;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isDeleted;

<<<<<<< HEAD
    public ProjectItem toEntity(ProjectManager projectManager) {
        return ProjectItem.builder()
                .projectItemId(this.projectItemId)
                .projectManager(projectManager)
=======
    public ProjectItem toEntity(ProjectManagerRepository projectManagerRepository) {
        Optional<ProjectManager> optionalProjectManager = projectManagerRepository.findById(projectManagerId);

        ProjectManager projectManager = optionalProjectManager.orElseThrow(() -> new IllegalArgumentException(""));

        return ProjectItem.builder()
                .projectItemId(this.projectItemId)
                .projectManagerId(projectManager)
>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
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
<<<<<<< HEAD
                .projectManagerId(entity.getProjectManager().getProjectManagerId())
=======
                .projectManagerId(entity.getProjectManagerId().getProjectManagerId())
>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
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
