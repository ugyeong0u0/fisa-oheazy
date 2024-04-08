package com.fisa.wooriarte.projectItem.domain;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProjectItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long projectId;


    @JoinColumn // 수정 필요
    private Long businessId;

    @Column
    private String artistName;

    @Column
    private String intro;

    @Column
    private String phone;

    @Column
    private boolean approval;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime startDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime endDate;

    @Column
    private boolean isDeleted;

    public ProjectItemDTO toDTO() {
        return ProjectItemDTO.builder()
                .projectId(projectId)
                .businessId(businessId)
                .artistName(artistName)
                .intro(intro)
                .phone(phone)
                .approval(approval)
                .createdAt(createdAt)
                .startDate(startDate)
                .endDate(endDate)
                .isDeleted(isDeleted)
                .build();
    }

    public void updateIsDeleted() {
        this.isDeleted = true;
    }
}
