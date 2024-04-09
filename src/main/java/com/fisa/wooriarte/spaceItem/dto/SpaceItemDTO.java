package com.fisa.wooriarte.spaceItem.dto;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SpaceItemDTO {

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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    public SpaceItem toEntity(SpaceRentalRepository spaceRentalRepository) {
        Optional<SpaceRental> optionalSpaceRental = spaceRentalRepository.findById(this.spaceRentalId);

        SpaceRental spaceRental = optionalSpaceRental.orElseThrow(() -> new IllegalArgumentException(""));

        return SpaceItem.builder()
                .spaceItemId(this.spaceItemId) // 엔티티의 ID를 설정합니다. 새 엔티티를 생성하는 경우에는 이 필드를 생략할 수도 있습니다.
                .spaceRentalId(spaceRental)
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

    public static SpaceItemDTO fromEntity(SpaceItem entity) {
        return SpaceItemDTO.builder()
                .spaceItemId(entity.getSpaceItemId())
                .spaceRentalId(entity.getSpaceRentalId().getSpaceRentalId()) // SpaceRental 객체의 ID를 추출
                .intro(entity.getIntro())
                .hostName(entity.getHostName())
                .city(entity.getCity())
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
