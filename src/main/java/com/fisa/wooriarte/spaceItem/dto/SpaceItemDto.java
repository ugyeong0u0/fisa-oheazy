package com.fisa.wooriarte.spaceItem.dto;

import com.fisa.wooriarte.spaceItem.domain.City;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SpaceItemDto {

    private Long spaceItemId;

    private Long spaceRentalId;

    private String intro;

    private String hostName;

    private String city;

    private String size;

    private Boolean parking;

    private int fee;

    private String phone;

    private Boolean approval;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate createdAt;

    private Boolean isDeleted;

    public SpaceItem toEntity(SpaceRental spaceRental) {
        return SpaceItem.builder()
                .spaceRental(spaceRental)
                .intro(this.intro)
                .hostName(this.hostName)
                .city(City.valueOf(this.city))
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

    public static SpaceItemDto fromEntity(SpaceItem entity) {
        return SpaceItemDto.builder()
                .spaceItemId(entity.getSpaceItemId())
                .spaceRentalId(entity.getSpaceRental().getSpaceRentalId()) // SpaceRental 객체의 ID를 추출
                .intro(entity.getIntro())
                .hostName(entity.getHostName())
                .city(entity.getCity().name())
                .size(entity.getSize())
                .parking(entity.getParking())
                .fee(entity.getFee())
                .phone(entity.getPhone())
                .approval(entity.getApproval())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }

}
