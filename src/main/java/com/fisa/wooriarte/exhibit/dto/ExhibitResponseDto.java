package com.fisa.wooriarte.exhibit.dto;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExhibitResponseDto {
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
    private List<String> urls;

    public static ExhibitResponseDto fromEntity(Exhibit exhibit) {
        return ExhibitResponseDto.builder()
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
                .city(exhibit.getCity().toString())
                .urls(exhibit.getMatching().getProjectItem().getProjectPhotos().stream().map(ProjectPhoto::getUrl).collect(Collectors.toList())) // 스트림의 결과를 리스트로 수집
                .build();
    }
}
