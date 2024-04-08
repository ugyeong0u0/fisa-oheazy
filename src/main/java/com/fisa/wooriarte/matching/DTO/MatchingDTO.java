package com.fisa.wooriarte.matching.DTO;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MatchingDTO {
    private Long matchingId;
    private MatchingStatus matchingStatus;
    private Long receiver;
    private Long sender;
    private LocalDate createAt;
    private Long projectId;
    private Long spaceId;
    private SenderType senderType;

    public Matching toEntity() {
        return Matching.builder()
                .matchingStatus(this.matchingStatus)
                .receiver(this.receiver)
                .sender(this.sender)
                .projectId(this.projectId)
                .spaceId(this.spaceId)
                .senderType(this.senderType)
                .build();
    }

    public static MatchingDTO fromEntity(Matching matching) {
        return MatchingDTO.builder()
                .matchingStatus(matching.getMatchingStatus())
                .receiver(matching.getReceiver())
                .sender(matching.getSender())
                .projectId(matching.getProjectId())
                .spaceId(matching.getSpaceId())
                .senderType(matching.getSenderType())
                .build();
    }

}
