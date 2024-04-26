package com.fisa.wooriarte.matching.dto;

import com.fisa.wooriarte.matching.domain.Matching;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchingResponseDto {
    private Long matchingId;
    private Long itemId;
    private String name;

    public static MatchingResponseDto fromEntityBySpaceRental(Matching matching) {
        return new MatchingResponseDto(matching.getMatchingId(), matching.getProjectItem().getProjectItemId(), matching.getProjectItem().getArtistName());
    }

    public static MatchingResponseDto fromEntityByProjectManager(Matching matching) {
        return new MatchingResponseDto(matching.getMatchingId(), matching.getSpaceItem().getSpaceItemId(), matching.getSpaceItem().getHostName());
    }
}
