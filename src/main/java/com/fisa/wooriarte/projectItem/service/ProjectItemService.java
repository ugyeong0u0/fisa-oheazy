package com.fisa.wooriarte.projectItem.service;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.SpaceRentalDto;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public void addProjectItem(SpaceRentalDto projectItemDTO){
        System.out.println("addProjectItem");
        ProjectManager projectManager = projectManagerRepository.findById(projectItemDTO.getProjectManagerId())
                .orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        projectItemRepository.save(projectItemDTO.toEntity(projectManager));
    }

    // 프로젝트 아이템 전체 조회
    public List<SpaceRentalDto> findAll() {
        System.out.println("findAll");
        return projectItemRepository.findAll().stream()
                .map(SpaceRentalDto::fromEntity) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    // 프로젝트 아이템 조회
    public Optional<SpaceRentalDto> findByProjectItemId(Long projectItemId) {
        System.out.println("findByProjectItemId");
        return projectItemRepository.findById(projectItemId)
                .map(SpaceRentalDto::fromEntity);
    }

    public List<SpaceRentalDto> findByProjectManagerId(Long projectManagerId) {
        // Optional<List<ProjectItem>>에서 List<ProjectItem>을 얻기 위해 orElseGet을 사용합니다.
        // Optional이 비어있다면, 빈 리스트를 반환합니다.
        ProjectManager projectManager = projectManagerRepository.findById(projectManagerId).orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        List<ProjectItem> projectItems = projectItemRepository.findByProjectManager(projectManager)
                .orElse(Collections.emptyList());

        // Stream을 사용하여 각 ProjectItem을 ProjectItemDTO로 변환합니다.
        return projectItems.stream()
                .map(SpaceRentalDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProjectItem(Long projectItemId, SpaceRentalDto projectItemDTO) {
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
