package com.fisa.wooriarte.exhibit.domain;

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
@Setter
@Builder
@ToString
@DynamicUpdate
public class Exhibit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long exhibitId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private Matching matching;

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
    private long price;

    @Column(nullable = false)
    private long soldAmount;

    @Column(nullable = false)
    private City city;

    @Column(nullable = false)
    private boolean deleted;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

}
