package com.fisa.wooriarte.ticket.service;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    //user id 값으로 모든 ticket list 검색 service
    //status unused/used 조건문
    public List<TicketDTO> getAllTicketsByUserId(long userId, boolean status){
        List<Ticket> ticketAll = TicketRepository.findByUserId(userId);

        if(!status){
            ticketAll = ticketAll.stream()
                    .filter(ticket -> !ticket.isStatus())
                    .collect(Collectors.toList());
        }

        return ticketAll.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());

    }

    //ticket 생성

    //ticket 삭제

}
