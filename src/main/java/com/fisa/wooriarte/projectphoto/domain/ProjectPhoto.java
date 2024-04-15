package com.fisa.wooriarte.projectphoto.domain;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProjectPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPhotoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectItem_id", nullable = false)
    private ProjectItem projectItem;

    // S3 키 값
    @Column(nullable = false)
    private String s3KeyName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;
}