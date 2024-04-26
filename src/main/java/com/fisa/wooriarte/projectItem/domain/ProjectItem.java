package com.fisa.wooriarte.projectItem.domain;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @OneToMany(mappedBy = "projectItem", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProjectPhoto> projectPhotos = new ArrayList<>();

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
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private City city;

    private String itemType;

    public void setIsDeleted() {
        this.isDeleted = true;
    }

    public void setApproval() { this.approval = true; }


    public void updateProjectItem(ProjectItemDto projectItemDto) {
        this.artistName = projectItemDto.getArtistName();
        this.intro = projectItemDto.getIntro();
        this.phone = projectItemDto.getPhone();
        this.startDate = projectItemDto.getStartDate();
        this.endDate = projectItemDto.getEndDate();
        this.city = City.valueOf(projectItemDto.getCity());

    }
}
