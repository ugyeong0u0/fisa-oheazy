package com.fisa.wooriarte.projectphoto.dto;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectPhotoDTO {

    private Long projectPhotoId;
    private Long projectItemId;
    private String fileName;
    private String url;
    private String s3KeyName; // S3 키 값을 저장하는 필드 추가

    public ProjectPhoto toEntity(ProjectItemRepository projectItemRepository) {
        // projectItemId를 찾아서 Optional로 받음
        Optional<ProjectItem> optionalProjectItem = projectItemRepository.findById(this.projectItemId);

        // Optional에서 SpaceItem이 존재하지 않으면 예외 발생
        ProjectItem projectItem = optionalProjectItem.orElseThrow(() -> new IllegalArgumentException("SpaceItem not found with id: " + this.projectItemId));
        return ProjectPhoto.builder()
                .projectItem(projectItem)
                .fileName(this.fileName)
                .url(this.url)
                .s3KeyName(this.s3KeyName) // s3KeyName 값도 함께 설정
                .build();
    }

    public static ProjectPhotoDTO fromEntity(ProjectPhoto projectPhoto) {
        if (projectPhoto == null) {
            return null;
        }
        return ProjectPhotoDTO.builder()
                .projectPhotoId(projectPhoto.getProjectPhotoId())
                .projectItemId(projectPhoto.getProjectItem().getProjectItemId())
                .fileName(projectPhoto.getFileName())
                .url(projectPhoto.getUrl())
                .s3KeyName(projectPhoto.getS3KeyName()) // s3KeyName 값도 함께 설정
                .build();
    }
}
