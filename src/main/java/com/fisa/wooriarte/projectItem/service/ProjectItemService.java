package com.fisa.wooriarte.projectItem.service;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.dto.ProjectItemResponseDto;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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

    /**
     * 프로젝트 아이템을 추가
     * @param projectItemDTO 추가할 프로젝트 아이템 정보
     * @return 추가된 프로젝트 아이템의 ID
     */
    @Transactional
    public Long addProjectItem(ProjectItemDto projectItemDTO) {
        ProjectManager projectManager = projectManagerRepository.findById(projectItemDTO.getProjectManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 관리자를 찾을 수 없습니다."));
        ProjectItem projectItem = projectItemDTO.toEntity(projectManager);
        projectItemRepository.save(projectItem);
        return projectItem.getProjectItemId();
    }

    /**
     * 승인된 모든 프로젝트 아이템 조회
     * @return 승인된 프로젝트 아이템 리스트
     */
    public List<ProjectItemResponseDto> findApprovedAll() {
        List<ProjectItem> projectItems = projectItemRepository.findAllByIsDeletedFalseAndApprovalTrue()
                .orElseThrow(() -> new NoSuchElementException("승인된 프로젝트 아이템이 없습니다."));
        return projectItems.stream()
                .map(ProjectItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 승인되지 않은 모든 프로젝트 아이템 조회
     * @return 승인되지 않은 프로젝트 아이템 리스트
     */
    public List<ProjectItemResponseDto> findUnapprovedAll() {
        List<ProjectItem> projectItems = projectItemRepository.findAllByIsDeletedFalseAndApprovalFalse()
                .orElseThrow(() -> new NoSuchElementException("승인되지 않은 프로젝트 아이템이 없습니다."));
        return projectItems.stream()
                .map(ProjectItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID의 프로젝트 아이템 조회
     * @param projectItemId 조회할 프로젝트 아이템 ID
     * @return 조회된 프로젝트 아이템 정보
     */
    public ProjectItemDto findByProjectItemId(Long projectItemId) {
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndIsDeletedFalse(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));
        return ProjectItemDto.fromEntity(projectItem);
    }

    /**
     * 특정 프로젝트 매니저 ID에 속한 프로젝트 아이템 조회
     * @param projectManagerId 프로젝트 관리자 ID
     * @return 해당 프로젝트 매니저에 속한 프로젝트 아이템 리스트
     */
    public List<ProjectItemResponseDto> findByProjectManagerId(Long projectManagerId) {
        ProjectManager projectManager = projectManagerRepository.findById(projectManagerId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 관리자를 찾을 수 없습니다."));
        List<ProjectItem> projectItems = projectItemRepository.findByProjectManagerAndIsDeletedFalseAndApprovalTrue(projectManager)
                .orElse(Collections.emptyList());

        return projectItems.stream()
                .map(ProjectItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 프로젝트 아이템 정보 수정
     * @param projectItemId 수정할 프로젝트 아이템 ID
     * @param projectItemDTO 수정할 프로젝트 아이템 정보
     */
    @Transactional
    public void updateProjectItem(Long projectItemId, ProjectItemDto projectItemDTO) {
        ProjectItem projectItem = projectItemRepository.findById(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));
        projectItem.updateProjectItem(projectItemDTO);
        projectItemRepository.save(projectItem);
    }

    /**
     * 프로젝트 아이템 삭제
     * @param projectItemId 삭제할 프로젝트 아이템 ID
     */
    @Transactional
    public void deleteProjectItem(Long projectItemId) {
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndIsDeletedFalse(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));
        projectItem.setIsDeleted();
        projectItemRepository.save(projectItem);
    }

    /**
     * 필터 조건에 따라 프로젝트 아이템 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param city 도시 이름
     * @return 필터 조건에 맞는 프로젝트 아이템 리스트
     */
    public List<ProjectItemResponseDto> findByFilter(LocalDate startDate, LocalDate endDate, String city) {
        List<ProjectItem> projectItems = projectItemRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqualAndCityAndIsDeletedFalseAndApprovalTrue(startDate, endDate, City.valueOf(city))
                .orElse(Collections.emptyList());
        return projectItems.stream().map(ProjectItemResponseDto::fromEntity).collect(Collectors.toList());
    }

    /**
     * 프로젝트 아이템 승인
     * @param projectItemId 승인할 프로젝트 아이템 ID
     * @return 승인 성공 여부
     */
    @Transactional
    public boolean approveItem(Long projectItemId) {
        ProjectItem projectItem = projectItemRepository.findById(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));
        projectItem.setApproval();
        projectItemRepository.save(projectItem);
        return true;
    }

    /**
     * 프로젝트 아이템 거절 처리
     * @param projectItemId 거절할 프로젝트 아이템 ID
     * @return 거절 성공 여부
     */
    @Transactional
    public boolean refuseItem(Long projectItemId) {
        projectItemRepository.deleteById(projectItemId);
        return true;
    }

    /**
     * 승인되지 않은 모든 프로젝트 아이템 조회
     * @return 승인되지 않은 프로젝트 아이템 리스트
     */
    public List<ProjectItemDto> getUnapprovedItems() {
        List<ProjectItem> projectItems = projectItemRepository.findAllByApprovalFalseAndIsDeletedFalse()
                .orElseThrow(() -> new NoSuchElementException("승인되지 않은 프로젝트 아이템이 없습니다."));
        return projectItems.stream()
                .map(ProjectItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}