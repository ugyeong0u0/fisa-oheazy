package com.fisa.wooriarte.ticket.service;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    //user id 값으로 unused ticket list 검색
//    public List<TicketDTO> getAllUnusedTicketsByUserId(long userId) {
//        List<Ticket> unusedTickets = ticketRepository.findByUserIdAndStatusAndCanceled(userId, true, false);
//        return unusedTickets.stream()
//                .map(TicketDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    //user id값으로 used ticket list 검색
//    public List<TicketDTO> getAllUsedTicketsByUserId(long userId) {
//        List<Ticket> usedTickets = ticketRepository.findByUserIdAndStatusAndCanceled(userId, true, true);
//        return usedTickets.stream()
//                .map(TicketDTO::fromEntity)
//                .collect(Collectors.toList());
//    }

    //새로운 ticket 생성
    public TicketDTO createTicket(TicketDTO ticketDTO){
        //TicketDTO -> 엔티티 변환
        Ticket ticket = ticketDTO.toEntity();
        //Ticket 엔티티 저장
        Ticket savedTicket = ticketRepository.save(ticket);
        //Ticket 엔티티 -> TicketDTO 변환
        return TicketDTO.fromEntity(savedTicket);
    }

    //ticket_id로 ticket 검색 후 canceled 상태 true로 변경
    @Transactional
    public void deleteTicketById(long ticketId){
        //ticket_id로 검색
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        //ticket이 존재할 경우 canceled 컬럼을 true로 변경
        optionalTicket.ifPresent(Ticket::cancel);
    }

    //ticket_id로 ticket 검색 후 status 상태 true로 변경
    @Transactional
    public boolean isTicketUsed(long ticketId){
        //ticket_id로 검색
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        //ticket이 존재하고 사용여부가 false일 경우 true로 변경
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            if(!ticket.isStatus()){
                ticket.use();
                ticketRepository.save(ticket);
                return true;
            }
        }
        return false;
    }
}
