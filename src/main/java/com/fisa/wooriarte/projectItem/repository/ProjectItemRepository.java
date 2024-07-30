package com.fisa.wooriarte.projectItem.repository;

import com.fisa.wooriarte.projectItem.domain.City;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    Optional<ProjectItem> findByProjectItemIdAndIsDeletedFalse(Long projectItemId);
    Optional<List<ProjectItem>> findByProjectManagerAndIsDeletedFalseAndApprovalTrue(ProjectManager projectManager);
    Optional<List<ProjectItem>> findAllByIsDeletedFalseAndApprovalTrue();
    Optional<List<ProjectItem>> findAllByIsDeletedFalseAndApprovalFalse();
    Optional<List<ProjectItem>> findAllByApprovalFalseAndIsDeletedFalse();

    Optional<List<ProjectItem>> findByEndDateGreaterThanEqualAndStartDateLessThanEqualAndCityAndIsDeletedFalseAndApprovalTrue(LocalDate startDate, LocalDate endDate, City city);
}
