package com.fisa.wooriarte.spaceItem.dto;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceItemResponseDto {

    private Long spaceItemId;
    private Long spaceRentalId;
    private String intro;
    private String hostName;
    private String city;
    private String size;
    private Boolean parking;
    private Boolean approval;
    private Long fee;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String itemType;
    private String url;

    public static SpaceItemResponseDto fromEntity(SpaceItem spaceItem) {
        return SpaceItemResponseDto.builder()
                .spaceItemId(spaceItem.getSpaceItemId())
                .spaceRentalId(spaceItem.getSpaceRental().getSpaceRentalId())
                .intro(spaceItem.getIntro())
                .hostName(spaceItem.getHostName())
                .city(spaceItem.getCity().toString())
                .size(spaceItem.getSize())
                .parking(spaceItem.getParking())
                .approval(spaceItem.getApproval())
                .fee(spaceItem.getFee())
                .phone(spaceItem.getPhone())
                .startDate(spaceItem.getStartDate())
                .endDate(spaceItem.getEndDate())
                .itemType(spaceItem.getItemType())
                .url(spaceItem.getSpacePhoto().isEmpty() ? "" : spaceItem.getSpacePhoto().get(0).getUrl())
                .build();
    }
}
