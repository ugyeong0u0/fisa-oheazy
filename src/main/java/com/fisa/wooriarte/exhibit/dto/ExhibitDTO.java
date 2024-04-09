package com.fisa.wooriarte.exhibit.dto;

import com.fisa.wooriarte.exhibit.domain.City;
import com.fisa.wooriarte.exhibit.domain.Exhibit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Builder
public class ExhibitDTO {
    private Long exhibitId;
    private Long matchingId;
    private String name;
    private String intro;
    private Date startDate;
    private Date endDate;
    private String artistName;
    private String hostName;
    private Long price;
    private Long soldAmount;
    private String city;
    private Boolean deleted;

    public Exhibit toEntity(){
        return Exhibit.builder()
                .exhibitId(this.exhibitId)
                .name(this.name)
                .intro(this.intro)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .artistName(this.artistName)
                .hostName(this.hostName)
                .price(this.price)
                .soldAmount(this.soldAmount)
                .city(City.valueOf(this.city))
                .deleted(this.deleted)
                .build();
    }

    public static ExhibitDTO fromEntity(Exhibit exhibit) {
        if (exhibit == null) {
            return null;
        }

        return ExhibitDTO.builder()
                .exhibitId(exhibit.getExhibitId())
                .name(exhibit.getName())
                .intro(exhibit.getIntro())
                .startDate(exhibit.getStartDate())
                .endDate(exhibit.getEndDate())
                .artistName(exhibit.getArtistName())
                .hostName(exhibit.getHostName())
                .price(exhibit.getPrice())
                .soldAmount(exhibit.getSoldAmount())
                .city(exhibit.getCity().name())
                .deleted(exhibit.getDeleted())
//                .matching_id((exhibit.getMatching() != null) ? exhibit.getMatching().getId() : null)
                .build();
    }
}
