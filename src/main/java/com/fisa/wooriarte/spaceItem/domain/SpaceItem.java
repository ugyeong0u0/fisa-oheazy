package com.fisa.wooriarte.spaceItem.domain;

import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SpaceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long spaceItemId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SpaceRental spaceRentalId;

    @OneToMany(mappedBy = "spaceItemId", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Matching> matchings = new ArrayList<>();

    @Column
    private String intro;

    @Column
    private String hostName;

    @Column
    private String city;

    @Column
    private String size;

    @Column
    private Boolean parking;

    @Column
    private int fee;

    @Column
    private String phone;

    @Column
    private Boolean approval;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime startDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime endDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private Boolean isDeleted;


    public void setIsDeleted() {
        this.isDeleted = true;
    }
}
