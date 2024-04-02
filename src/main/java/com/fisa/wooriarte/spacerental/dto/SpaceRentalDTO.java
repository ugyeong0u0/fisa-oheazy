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
    private long business_id;
    private long business_number;
    private String id;
    private String pwd;
    private String company;
    private String ceo;
    private String email;
    private String phone;
    private LocalDateTime create_at;
    private boolean deleted;

    public SpaceRental toEntity() {
        return SpaceRental.builder()
                .business_id(this.business_id)
                .business_number(this.business_number)
                .id(this.id)
                .pwd(this.pwd)
                .company(this.company)
                .ceo(this.ceo)
                .email(this.email)
                .phone(this.phone)
                .create_at(this.create_at)
                .deleted(this.deleted)
                .build();
    }
}
