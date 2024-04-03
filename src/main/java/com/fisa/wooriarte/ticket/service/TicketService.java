package com.fisa.wooriarte.ticket.service;

import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    // 사용 여부에 따라 티켓 리스트를 가져오는 메서드
    public List<TicketDTO> getTicketsByUserIdAndStatus(long userId, boolean status) {
        List<Ticket> tickets;
        if (status) {
            tickets = ticketRepository.findByUserIdAndStatusAndCanceled(String.valueOf(userId), true, false);
        } else {
            tickets = ticketRepository.findByUserIdAndStatusAndCanceled(String.valueOf(userId), false, false);
        }
        return tickets.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //새로운 ticket 생성
    public TicketDTO createTicket(TicketDTO ticketDTO){
        // userId를 사용하여 User 엔티티를 가져옴
        User user = userRepository.findById(Math.toIntExact(ticketDTO.getUser()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + ticketDTO.getUser()));
        //TicketDTO -> 엔티티 변환
        Ticket ticket = ticketDTO.toEntity(userRepository);
        //Ticket 엔티티 저장
        Ticket savedTicket = ticketRepository.save(ticket);
        //Ticket 엔티티 -> TicketDTO 변환
        return TicketDTO.fromEntity(savedTicket);
    }

    //티켓 취소 메소드
    //ticket_id로 ticket 검색 후 canceled 상태 true로 변경
    @Transactional
    public void deleteTicketById(long ticketId){
        //ticket_id로 검색
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        //ticket이 존재할 경우 canceled 컬럼을 true로 변경
        optionalTicket.ifPresent(Ticket::setCanceled);
    }

    //티켓 사용 여부를 변경하는 메소드
    //ticket_id로 ticket 검색 후 status 상태 변경
    @Transactional
    public boolean useTicketAndCheckIfUsed(long ticketId){
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            if(!ticket.isCanceled()){
                ticket.setstatus();
                ticketRepository.save(ticket);
                return true; // 티켓 status 상태 변경 완료
            } else {
                return false; // 티켓이 이미 사용되었거나 취소된 경우
            }
        } else {
            return false; // 티켓이 존재하지 않는 경우
        }
    }
}
