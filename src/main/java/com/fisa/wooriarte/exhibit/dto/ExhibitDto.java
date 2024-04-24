package com.fisa.wooriarte.exhibit.dto;

import com.fisa.wooriarte.exhibit.domain.City;
import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.repository.MatchingRepository;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;


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


    public Exhibit toEntity(MatchingRepository matchingRepository){
        // 매칭를 찾아서 Optional로 받음
        Optional<Matching> optionalMatching = matchingRepository.findById(this.matchingId);

        // Optional에서 매칭를 가져오거나 매칭이 존재하지 않으면 예외 발생
        Matching matching = optionalMatching.orElseThrow(() -> new IllegalArgumentException("Matching not found with id: " + this.matchingId));
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
//                .matching_id((exhibit.getMatching() != null) ? exhibit.getMatching().getId() : null)
                .build();
    }

    public void setMatchingId(Long matchingId) {
        this.matchingId=matchingId;
    }
}
