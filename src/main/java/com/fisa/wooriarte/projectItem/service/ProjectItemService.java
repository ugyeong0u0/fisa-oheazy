package com.fisa.wooriarte.projectItem.service;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.dto.ProjectItemResponseDto;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Long addProjectItem(ProjectItemDto projectItemDTO){
        System.out.println("addProjectItem");
        ProjectManager projectManager = projectManagerRepository.findById(projectItemDTO.getProjectManagerId())
                .orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        ProjectItem projectItem = projectItemDTO.toEntity(projectManager);
        projectItemRepository.save(projectItem);
        return projectItem.getProjectItemId();
    }

    // 프로젝트 아이템 전체 조회(삭제된 아이템 제외, 승인O)
    public List<ProjectItemResponseDto> findApprovedAll() {
        System.out.println("findAll");
        List<ProjectItem> projectItemList = projectItemRepository.findAllByIsDeletedFalseAndApprovalTrue().orElseThrow(() -> new NoSuchElementException("No Project Item"));
        return projectItemList.stream()
                .map(ProjectItemResponseDto::fromEntity) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    // 프로젝트 아이템 전체 조회(삭제된 아이템 제외, 승인X)
    public List<ProjectItemResponseDto> findUnapprovedAll() {
        System.out.println("findAll");
        List<ProjectItem> projectItemList = projectItemRepository.findAllByIsDeletedFalseAndApprovalFalse().orElseThrow(() -> new NoSuchElementException("No Project Item"));
        return projectItemList.stream()
                .map(ProjectItemResponseDto::fromEntity) // 람다식을 사용하여 각 ProjectItem 엔티티를 ProjectItemDTO로 변환
                .collect(Collectors.toList());
    }

    // 프로젝트 아이템 조회(삭제된 아이템 제외)
    public ProjectItemDto findByProjectItemId(Long projectItemId) {
        System.out.println("findByProjectItemId");
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndIsDeletedFalse(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("No Project Item found with ID: " + projectItemId));
        return ProjectItemDto.fromEntity(projectItem);
    }


    public List<ProjectItemResponseDto> findByProjectManagerId(Long projectManagerId) {
        // Optional<List<ProjectItem>>에서 List<ProjectItem>을 얻기 위해 orElseGet을 사용합니다.
        // Optional이 비어있다면, 빈 리스트를 반환합니다.
        ProjectManager projectManager = projectManagerRepository.findById(projectManagerId).orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        List<ProjectItem> projectItems = projectItemRepository.findByProjectManagerAndIsDeletedFalse(projectManager)
                .orElse(Collections.emptyList());

        // Stream을 사용하여 각 ProjectItem을 ProjectItemDTO로 변환합니다.
        return projectItems.stream()
                .map(ProjectItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProjectItem(Long projectItemId, ProjectItemDto projectItemDTO) {
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
        projectItemRepository.save(projectItem);
    }

    public List<ProjectItemResponseDto> findByFilter(LocalDate startDate, LocalDate endDate, String city) {
        List<ProjectItem> projectItems = projectItemRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqualAndCityAndIsDeletedFalseAndApprovalTrue(startDate, endDate, City.valueOf(city)).orElse(Collections.emptyList());
        return projectItems.stream().map(ProjectItemResponseDto::fromEntity).collect(Collectors.toList());
    }
    @Transactional
    public boolean approveItem(Long projectItemId) {
        ProjectItem projectItem = projectItemRepository.findById(projectItemId).orElseThrow(() -> new NoSuchElementException("No Project Item found with ID: " + projectItemId));
        projectItem.setApproval();
        projectItemRepository.save(projectItem);
        return true;
    }
    @Transactional
    public boolean refuseItem(Long projectItemId) {
        projectItemRepository.deleteById(projectItemId);
        return true;
    }

    public List<ProjectItemDto> getUnapprovedItems() {
        List<ProjectItem> projectItems = projectItemRepository.findAllByApprovalFalseAndIsDeletedFalse()
                .orElseThrow(() -> new NoSuchElementException("No Project Item"));

        return projectItems.stream()
                .map(ProjectItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}
