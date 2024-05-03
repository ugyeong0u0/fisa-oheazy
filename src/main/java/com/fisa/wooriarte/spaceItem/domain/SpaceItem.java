package com.fisa.wooriarte.spaceItem.domain;

import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spacephoto.domain.SpacePhoto;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
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
    private Long spaceItemId;

    @ManyToOne
    @JoinColumn(name = "space_rental_id", nullable = false)
    private SpaceRental spaceRental;

    @OneToMany(mappedBy = "spaceItem", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Matching> matchings = new ArrayList<>();

    @OneToMany(mappedBy = "spaceItem", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpacePhoto> spacePhoto = new ArrayList<>();

    @Column(length = 2000, columnDefinition = "text")
    private String intro;

    @Column
    private String hostName;

    @Column
    private City city;

    @Column
    private String size;

    @Column
    private Boolean parking;

    @Column
    private Long fee;

    @Column
    private String phone;

    @Column
    private Boolean approval;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
    private LocalDate endDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column
    private Boolean isDeleted;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private String itemType = "SpaceItem";

    public void setIsDeleted() {
        this.isDeleted = true;
    }

    public void setApproval() { this.approval = true; }

    public void updateSpaceItem(SpaceItemDto spaceItemDto) {
        this.intro = spaceItemDto.getIntro();
        this.hostName = spaceItemDto.getHostName();
        this.city = City.valueOf(spaceItemDto.getCity());
        this.size = spaceItemDto.getSize();
        this.parking = spaceItemDto.getParking();
        this.fee = spaceItemDto.getFee();
        this.phone = spaceItemDto.getPhone();
    }
}
