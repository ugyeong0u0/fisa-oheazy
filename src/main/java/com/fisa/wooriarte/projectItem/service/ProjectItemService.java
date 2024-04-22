package com.fisa.wooriarte.projectItem.service;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDTO;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectItemService {
    private final ProjectItemRepository projectItemRepository;
    private final ProjectManagerRepository projectManagerRepository;

    @Autowired
    public ProjectItemService(ProjectItemRepository projectItemRepository, ProjectManagerRepository projectManagerRepository) {
        this.projectItemRepository = projectItemRepository;
        this.projectManagerRepository = projectManagerRepository;
    }

    // 프로젝트 추가
    @Transactional
    public void addProjectItem(ProjectItemDTO projectItemDTO){
        System.out.println("addProjectItem");
        ProjectManager projectManager = projectManagerRepository.findById(projectItemDTO.getProjectManagerId())
                .orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        projectItemRepository.save(projectItemDTO.toEntity(projectManager));
    }

    // 프로젝트 아이템 전체 조회
    public List<ProjectItemDTO> findAll() {
        System.out.println("findAll");
        return projectItemRepository.findAll().stream()
                .map(ProjectItemDTO::fromEntity) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    // 프로젝트 아이템 조회
    public Optional<ProjectItemDTO> findByProjectItemId(Long projectItemId) {
        System.out.println("findByProjectItemId");
        return projectItemRepository.findById(projectItemId)
                .map(ProjectItemDTO::fromEntity);

    }
    @Transactional
    public void updateProjectItem(Long projectItemId, ProjectItemDTO projectItemDTO) {
        System.out.println("updateProjectItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        projectItemRepository.findById(projectItemId).ifPresent(existingItem -> {
            existingItem.updateProjectItem(projectItemDTO);
            // 변경된 엔티티 저장
            projectItemRepository.save(existingItem);
        });
    }

    @Transactional
    public void deleteProjectItem(Long projectItemId) throws Exception {
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndIsDeletedFalse(projectItemId)
                .orElseThrow(() -> new Exception("projectItem id: " + projectItemId + " 는 존재하지 않습니다"));
        projectItem.setIsDeleted();
    }

}
