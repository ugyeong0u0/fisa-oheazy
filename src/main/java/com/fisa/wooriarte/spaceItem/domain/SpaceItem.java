package com.fisa.wooriarte.spaceItem.domain;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
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
public class SpaceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long spaceId;


    @JoinColumn // 수정 필요
    private Long businessId;

    @Column
    private String intro;

    @Column
    private String hostName;

    @Column
    private String city;

    @Column
    private String size;

    @Column
    private boolean parking;

    @Column
    private int fee;

    @Column
    private String phone;

    @Column
    private boolean approval;

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
    private boolean isDeleted;

    public SpaceItemDTO toDTO() {
        return SpaceItemDTO.builder()
                .spaceId(this.spaceId)
                .businessId(this.businessId)
                .intro(this.intro)
                .hostName(this.hostName)
                .city(this.city)
                .size(this.size)
                .parking(this.parking)
                .fee(this.fee)
                .phone(this.phone)
                .approval(this.approval)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .createdAt(this.createdAt)
                .isDeleted(this.isDeleted)
                .build();
    }

    public void updateIsDeleted() {
        this.isDeleted = true;
    }
}
