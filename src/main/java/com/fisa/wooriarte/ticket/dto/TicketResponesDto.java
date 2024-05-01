package com.fisa.wooriarte.ticket.dto;

import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import com.fisa.wooriarte.ticket.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponesDto {
    private Long ticketId;
    private Long exhibitId;
    private String name;
    private Long amount;
    private String ticketNo;
    private String url;

    public static TicketResponesDto fromEntity(Ticket ticket) {
        List<ProjectPhoto> photos = ticket.getExhibit().getMatching().getProjectItem().getProjectPhotos();
        return TicketResponesDto.builder()
                .ticketId(ticket.getTicketId())
                .exhibitId(ticket.getExhibit().getExhibitId())
                .name(ticket.getExhibit().getName())
                .amount(ticket.getAmount())
                .ticketNo(ticket.getTicketNo())
                .url(photos.isEmpty() ? "" : photos.get(0).getUrl())
                .build();
    }
}
