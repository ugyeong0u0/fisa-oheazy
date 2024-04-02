package com.fisa.wooriarte.spacerental.domain;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
@DynamicUpdate
public class SpaceRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long business_id;

    @Column(nullable = false, unique = true)
    private long business_number;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_at;

    @Column(nullable = false)
    private boolean deleted;

    public SpaceRentalDTO toDTO() {
        return SpaceRentalDTO.builder()
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
