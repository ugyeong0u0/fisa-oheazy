package com.fisa.wooriarte.spacerental.dto;

import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SpaceRentalDTO {
    private long spaceRentalId;
    private long businessNumber;
    private String id;
    private String pwd;
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private LocalDateTime createAt;
    private boolean deleted;

    public SpaceRental toEntity() {
        return SpaceRental.builder()
                .spaceRentalId(this.spaceRentalId)
                .businessNumber(this.businessNumber)
                .id(this.id)
                .pwd(this.pwd)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .createAt(this.createAt)
                .deleted(this.deleted)
                .build();
    }
}
