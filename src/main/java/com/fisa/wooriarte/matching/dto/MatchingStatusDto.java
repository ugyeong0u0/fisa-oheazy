package com.fisa.wooriarte.matching.dto;

import com.fisa.wooriarte.matching.domain.MatchingStatus;

public class MatchingStatusDto {
    private MatchingStatus matchingStatus;

    // getter와 setter
    public MatchingStatus getMatchingStatus() {
        return matchingStatus;
    }

    public void setMatchingStatus(MatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
    }
}
