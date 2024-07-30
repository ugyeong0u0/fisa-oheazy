package com.fisa.wooriarte.exhibit.dto;

import com.fisa.wooriarte.exhibit.domain.City;
import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.repository.MatchingRepository;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Builder
public class ExhibitDto {
    private Long exhibitId;
    private Long matchingId;
    private String name;
    private String intro;
    private LocalDate startDate;
    private LocalDate endDate;
    private String artistName;
    private String hostName;
    private Long price;
    private Long soldAmount;
    private String city;
    private Boolean isDeleted;
    private List<String> urls;

    public Exhibit toEntity(Matching matching){
        return Exhibit.builder()
                .exhibitId(this.exhibitId)
                .matching(matching)
                .name(this.name)
                .intro(this.intro)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .artistName(this.artistName)
                .hostName(this.hostName)
                .price(this.price)
                .soldAmount(this.soldAmount)
                .city(City.valueOf(this.city))
                .isDeleted(this.isDeleted)
                .build();
    }

    public static ExhibitDto fromEntity(Exhibit exhibit) {
        if (exhibit == null) {
            return null;
        }
        return ExhibitDto.builder()
                .exhibitId(exhibit.getExhibitId())
                .matchingId(exhibit.getMatching().getMatchingId())
                .name(exhibit.getName())
                .intro(exhibit.getIntro())
                .startDate(exhibit.getStartDate())
                .endDate(exhibit.getEndDate())
                .artistName(exhibit.getArtistName())
                .hostName(exhibit.getHostName())
                .price(exhibit.getPrice())
                .soldAmount(exhibit.getSoldAmount())
                .city(exhibit.getCity().name())
                .isDeleted(exhibit.getIsDeleted())
                .urls(exhibit.getMatching().getProjectItem().getProjectPhotos().stream().map(ProjectPhoto::getUrl).collect(Collectors.toList()))
                .build();
    }

    public void setMatchingId(Long matchingId) {
        this.matchingId=matchingId;
    }
}
