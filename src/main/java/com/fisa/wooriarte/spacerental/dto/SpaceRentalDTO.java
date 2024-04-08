package com.fisa.wooriarte.spacerental.dto;

import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SpaceRentalDTO {
    private Long spaceRentalId;
    private Long businessNumber;
    private String id;
    private String pwd;
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private LocalDateTime createAt;
    private Boolean deleted;

    public SpaceRental toEntity() {
        return SpaceRental.builder()
                .businessNumber(this.businessNumber)
                .id(this.id)
                .pwd(this.pwd)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .deleted(this.deleted)
                .build();
    }

    public static SpaceRentalDTO fromEntity(SpaceRental spaceRental) {
        return SpaceRentalDTO.builder()
                .businessNumber(spaceRental.getBusinessNumber())
                .id(spaceRental.getId())
                .company(spaceRental.getCompany())
                .ceo(spaceRental.getCeo())
                .email(spaceRental.getEmail())
                .phone(spaceRental.getPhone())
                .deleted(spaceRental.getDeleted())
                .build();
    }
}
