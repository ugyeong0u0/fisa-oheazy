package com.fisa.wooriarte.matching.domain;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;

    @OneToMany(mappedBy = "matching", fetch = FetchType.LAZY)
    private List<Exhibit> exhibits;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SpaceItem spaceItemId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProjectItem projectItemId;

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
