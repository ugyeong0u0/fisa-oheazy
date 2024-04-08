package com.fisa.wooriarte.matching.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    @Column(nullable = false, updatable = false)
    private Long receiver;

    @Column(nullable = false, updatable = false)
    private Long sender;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createAt;

    @Column(nullable = false, updatable = false)
    private Long projectId;

    @Column(nullable = false, updatable = false)
    private Long spaceId;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    public void setMatchingStatus(MatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
    }
}
