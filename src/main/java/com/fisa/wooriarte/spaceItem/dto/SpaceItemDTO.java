package com.fisa.wooriarte.spaceItem.dto;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SpaceItemDTO {

    private Long spaceId;

    private Long businessId;

    private String intro;

    private String hostName;

    private String city;

    private String size;

    private boolean parking;

    private int fee;

    private String phone;

    private boolean approval;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    private boolean isDeleted;

    public SpaceItem toEntity() {
        return SpaceItem.builder()
                .spaceId(this.spaceId) // 엔티티의 ID를 설정합니다. 새 엔티티를 생성하는 경우에는 이 필드를 생략할 수도 있습니다.
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
}
