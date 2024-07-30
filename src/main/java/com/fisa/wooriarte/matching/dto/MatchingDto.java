package com.fisa.wooriarte.matching.dto;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MatchingDto {
    private Long matchingId;
    private MatchingStatus matchingStatus;
    private Long spaceItemId;
    private Long projectItemId;
    private Long receiver;
    private Long sender;
    private LocalDate createAt;
    private SenderType senderType;

    public Matching toEntity(SpaceItem spaceItem, ProjectItem projectItem) {
        return Matching.builder()
                .matchingId(this.matchingId)
                .spaceItem(spaceItem)
                .projectItem(projectItem)
                .matchingStatus(this.matchingStatus)
                .receiver(this.receiver)
                .sender(this.sender)
                .senderType(this.senderType)
                .build();
    }

    public static MatchingDto fromEntity(Matching matching) {
        return MatchingDto.builder()
                .matchingId(matching.getMatchingId())
                .matchingStatus(matching.getMatchingStatus())
                .spaceItemId(matching.getSpaceItem().getSpaceItemId())
                .projectItemId(matching.getProjectItem().getProjectItemId())
                .receiver(matching.getReceiver())
                .sender(matching.getSender())
                .senderType(matching.getSenderType())
                .build();
    }

}
