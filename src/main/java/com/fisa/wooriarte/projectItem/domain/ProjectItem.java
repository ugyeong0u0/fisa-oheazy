package com.fisa.wooriarte.projectItem.domain;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDTO;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProjectItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long projectItemId;

    // Many : projectManager / One :
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_manager_id", nullable = false) // 수정 필요
    private ProjectManager projectManager;

    @OneToMany(mappedBy = "projectItem", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Matching> matchings = new ArrayList<>();

    @Column(nullable = false)
    private String artistName;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Boolean approval;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime startDate;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void setIsDeleted() {
        this.isDeleted = true;
    }


    public void updateProjectItem(ProjectItemDTO projectItemDTO) {
        this.artistName = projectItemDTO.getArtistName();
        this.intro = projectItemDTO.getIntro();
        this.phone = projectItemDTO.getPhone();
        this.startDate = projectItemDTO.getStartDate();
        this.endDate = projectItemDTO.getEndDate();
    }
}
