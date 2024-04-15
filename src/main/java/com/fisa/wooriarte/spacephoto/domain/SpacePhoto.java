package com.fisa.wooriarte.spacephoto.domain;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
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
public class SpacePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spacePhotoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spaceItem_id", nullable = false)
    private SpaceItem spaceItem;

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
