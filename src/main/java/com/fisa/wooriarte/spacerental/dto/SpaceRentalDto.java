package com.fisa.wooriarte.spacerental.dto;

import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpaceRentalDto {
    private Long spaceRentalId;
    private Long businessNumber;
    private String id;
    private String pwd;
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private Boolean isDeleted;

    public SpaceRental toEntity() {
        return SpaceRental.builder()
                .businessNumber(this.businessNumber)
                .id(this.id)
                .pwd(this.pwd)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .isDeleted(this.isDeleted)
                .build();
    }

    public static SpaceRentalDto fromEntity(SpaceRental spaceRental) {
        return SpaceRentalDto.builder()
                .spaceRentalId(spaceRental.getSpaceRentalId())
                .businessNumber(spaceRental.getBusinessNumber())
                .id(spaceRental.getId())
                .company(spaceRental.getCompany())
                .ceo(spaceRental.getCeo())
                .email(spaceRental.getEmail())
                .phone(spaceRental.getPhone())
                .isDeleted(spaceRental.getIsDeleted())
                .build();
    }
}
