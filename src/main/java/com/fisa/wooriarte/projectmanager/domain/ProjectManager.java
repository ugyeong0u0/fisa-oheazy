package com.fisa.wooriarte.projectmanager.domain;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.DTO.ProjectManagerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long projectManagerId;

    @OneToMany(mappedBy = "projectManagerId", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProjectItem> projectItems = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long businessNumber;

    @Column(nullable = false, unique = true)
    private String id;


    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void setIsDeleted(){
        this.isDeleted=!this.isDeleted;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void updateProjectManager(ProjectManagerDTO projectManagerDTO) {
        this.businessNumber = projectManagerDTO.getBusinessNumber();
        this.company = projectManagerDTO.getCompany();
        this.ceo = projectManagerDTO.getCeo();
        this.email = projectManagerDTO.getEmail();
        this.phone = projectManagerDTO.getPhone();
    }
}
