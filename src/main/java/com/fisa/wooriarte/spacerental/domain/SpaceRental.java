package com.fisa.wooriarte.spacerental.domain;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SpaceRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long spaceRentalId;

    @OneToMany(mappedBy = "spaceRental", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpaceItem> spaceItems = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long businessNumber;

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
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void setIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }

    public void setPwd(String pwd) { this.pwd = pwd; }

    public void updateSpaceRental(SpaceRentalDTO spaceRentalDTO) {
        this.businessNumber = spaceRentalDTO.getBusinessNumber();
        this.company = spaceRentalDTO.getCompany();
        this.ceo = spaceRentalDTO.getCeo();
        this.email = spaceRentalDTO.getEmail();
        this.phone = spaceRentalDTO.getPhone();
        this.id = spaceRentalDTO.getId();
        this.pwd = spaceRentalDTO.getPwd();
    }
}
