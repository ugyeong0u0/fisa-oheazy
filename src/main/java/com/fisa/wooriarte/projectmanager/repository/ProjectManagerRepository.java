package com.fisa.wooriarte.projectmanager.repository;

import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Long> {

    @Query(value = "select * from project_manager p where p.id =:id", nativeQuery = true)
    public Optional<ProjectManager> findByProjectManagerId(String id);
}
