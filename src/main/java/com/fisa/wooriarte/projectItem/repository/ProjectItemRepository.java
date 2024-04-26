package com.fisa.wooriarte.projectItem.repository;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    Optional<ProjectItem> findByProjectItemIdAndIsDeletedFalse(Long projectItemId);
    Optional<List<ProjectItem>> findByProjectManager(ProjectManager projectManager);
    Optional<List<ProjectItem>> findAllByIsDeletedFalse();
    Optional<List<ProjectItem>> findByStartDateGreaterThanEqualAndEndDateLessThanEqualAndCity(LocalDate startDate, LocalDate endDate, City city);
    Optional<List<ProjectItem>> findAllByApprovalFalse();
}
