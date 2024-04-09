package com.fisa.wooriarte.spacerental.domain;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
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
    private Long spaceRentalId;

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
    private Boolean deleted;

    @OneToMany(mappedBy = "spaceRentalId", fetch = FetchType.LAZY)
    private List<SpaceItem> spaceItems = new ArrayList<>();

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setPwd(String pwd) { this.pwd = pwd; }

    public void updateSpaceRental(SpaceRentalDTO spaceRentalDTO) {
        this.businessNumber = spaceRentalDTO.getBusinessNumber();
        this.company = spaceRentalDTO.getCompany();
        this.ceo = spaceRentalDTO.getCeo();
        this.email = spaceRentalDTO.getEmail();
        this.phone = spaceRentalDTO.getPhone();
    }
}
