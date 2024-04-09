package com.fisa.wooriarte.exhibit.domain;

import com.fisa.wooriarte.matching.domain.Matching;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicUpdate
public class Exhibit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exhibitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Matching matching;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 65535)
    private String intro;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(nullable = false)
    private String artistName;

    @Column(nullable = false)
    private String hostName;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long soldAmount;

    @Column(nullable = false)
    private City city;

    @Column(nullable = false)
    private Boolean deleted;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

    //티켓 삭제 deleted 컬럼 변경
    public void setDeleted() {
        this.deleted = !this.deleted;
    }

}
