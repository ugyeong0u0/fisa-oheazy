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
    public boolean addProjectItem(ProjectItemDTO projectItemDTO){
        System.out.println("addProjectItem");
        projectItemRepository.save(projectItemDTO.toEntity());
        return true;
    }

    public List<ProjectItemDTO> findAll() {
        System.out.println("findAll");
        return projectItemRepository.findAll().stream()
                .map(ProjectItem::toDTO) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<ProjectItemDTO> findProjectItembyId(Long projectId) {
        System.out.println("findProjectItemById");
        return projectItemRepository.findByProjectId(projectId)
                .map(ProjectItem::toDTO);

    }
    @Transactional
    public void updateProjectItem(Long projectId, ProjectItemDTO projectItemDTO) {
        System.out.println("updateProjectItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        projectItemRepository.findByProjectId(projectId).ifPresent(existingItem -> {
            // DTO에서 엔티티로 프로퍼티 복사, "id", "createdAt" 등 변경되면 안되는 필드는 제외
            BeanUtils.copyProperties(projectItemDTO, existingItem, "id", "createdAt");
            // 변경된 엔티티 저장
            projectItemRepository.save(existingItem);
        });
    }

    @Transactional
    public void deleteProjectItem(Long projectId) throws Exception {
        ProjectItem projectItem = projectItemRepository.findByProjectIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new Exception("projectItem id: " + projectId + " 는 존재하지 않습니다"));
        projectItem.updateIsDeleted();
    }

}
