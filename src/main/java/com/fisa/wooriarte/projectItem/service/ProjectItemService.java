package com.fisa.wooriarte.projectItem.service;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDTO;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectItemService {
    private final ProjectItemRepository projectItemRepository;

    @Autowired
    public ProjectItemService(ProjectItemRepository projectItemRepository) {
        this.projectItemRepository = projectItemRepository;
    }

    @Transactional
    public void addProjectItem(ProjectItemDTO projectItemDTO){
        System.out.println("addProjectItem");
        projectItemRepository.save(projectItemDTO.toEntity());
    }

    public List<ProjectItemDTO> findAll() {
        System.out.println("findAll");
        return projectItemRepository.findAll().stream()
                .map(ProjectItemDTO::fromEntity) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<ProjectItemDTO> findByProjectItemId(Long projectItemId) {
        System.out.println("findByProjectItemId");
        return projectItemRepository.findById(projectItemId)
                .map(ProjectItemDTO::fromEntity);

    }
    @Transactional
    public void updateProjectItem(Long projectItemId, ProjectItemDTO projectItemDTO) {
        System.out.println("updateProjectItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        projectItemRepository.findByProjectItemId(projectItemId).ifPresent(existingItem -> {

            // DTO에서 엔티티로 프로퍼티 복사, "id", "createdAt" 등 변경되면 안되는 필드는 제외
            BeanUtils.copyProperties(projectItemDTO, existingItem, "id", "createdAt");
            // 변경된 엔티티 저장
            projectItemRepository.save(existingItem);
        });
    }

    @Transactional
    public void deleteProjectItem(Long projectItemId) throws Exception {
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndIsDeletedFalse(projectItemId)
                .orElseThrow(() -> new Exception("projectItem id: " + projectItemId + " 는 존재하지 않습니다"));

        projectItem.updateIsDeleted();
    }

}
