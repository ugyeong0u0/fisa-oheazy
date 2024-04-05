package com.fisa.wooriarte.matching.DTO;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
                .matchingId(this.matchingId)
                .matchingStatus(this.matchingStatus)
                .receiver(this.receiver)
                .sender(this.sender)
                .createAt(this.createAt)
                .projectId(this.projectId)
                .spaceId(this.spaceId)
                .senderType(this.senderType)
                .build();
    }
}
