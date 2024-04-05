package com.fisa.wooriarte.exhibit.dto;

import com.fisa.wooriarte.exhibit.domain.City;
import com.fisa.wooriarte.exhibit.domain.Exhibit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ExhibitDTO {
    private long exhibitId;
//    private long matchingId;
    private String name;
    private String intro;
    private Date startDate;
    private Date endDate;
    private String artistName;
    private String hostName;
    private long price;
    private long soldAmount;
    private String city;
    private boolean deleted;
    private LocalDateTime date;

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
                .date(this.date)
                .build();
    }

    public static ExhibitDTO fromEntity(Exhibit exhibit){
        ExhibitDTO dto = new ExhibitDTO();
        dto.setExhibitId(exhibit.getExhibitId());
        dto.setName(exhibit.getName());
        dto.setIntro(exhibit.getIntro());
        dto.setStartDate(exhibit.getStartDate());
        dto.setEndDate(exhibit.getEndDate());
        dto.setArtistName(exhibit.getArtistName());
        dto.setHostName(exhibit.getHostName());
        dto.setPrice(exhibit.getPrice());
        dto.setSoldAmount(exhibit.getSoldAmount());
        dto.setCity(exhibit.getCity().name());
        dto.setDeleted(exhibit.isDeleted());
        dto.setDate(exhibit.getDate());

//        if (exhhibit.getMatching() != null) {
//            dto.setMatching_id(exhibit.getMatching().getId());
//        }

        return dto;
    }
}
