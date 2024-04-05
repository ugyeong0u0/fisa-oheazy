package com.fisa.wooriarte.matching.domain;

import com.fisa.wooriarte.matching.DTO.MatchingDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(updatable = false)
    private LocalDate createAt;

    @Column(nullable = false, updatable = false)
    private Long projectId;

    @Column(nullable = false, updatable = false)
    private Long spaceId;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    public MatchingDTO toDTO() {
        return MatchingDTO.builder()
                .matchingId(this.matchingId)
                .matchingStatus(this.matchingStatus)
                .receiver(this.receiver)
                .sender(this.sender)
                .createAt(this.createAt)
                .projectId(this.projectId)
                .spaceId(this.spaceId)
                .senderType(this.senderType)
                .build();
    }
}
