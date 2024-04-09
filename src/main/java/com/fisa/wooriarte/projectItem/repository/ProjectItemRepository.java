package com.fisa.wooriarte.projectItem.repository;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    Optional<ProjectItem> findByProjectItemId(Long id);
    Optional<ProjectItem> findByProjectItemIdAndIsDeletedFalse(Long id);
}
