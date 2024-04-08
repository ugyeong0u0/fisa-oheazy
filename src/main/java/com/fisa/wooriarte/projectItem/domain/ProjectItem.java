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

    @Column(nullable = false)
    private String artistName;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private boolean approved;

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
    private boolean deleted;

    public void updateIsDeleted() {
        this.deleted = true;
    }
}
