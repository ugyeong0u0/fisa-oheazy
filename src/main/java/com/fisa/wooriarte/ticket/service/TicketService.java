package com.fisa.wooriarte.ticket.service;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.exhibit.repository.ExhibitRepository;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    private final ExhibitRepository exhibitRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, ExhibitRepository exhibitRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.exhibitRepository = exhibitRepository;
    }

    /**
     * 1. 사용 여부에 따라서 ticket 리스트 출력
     * @param userId : 티켓을 소유하고 있는 userId
     * @param status : 티켓의 사용 여부
     * @return
     */
    public List<TicketDTO> getTicketsByUserIdAndStatus(long userId, boolean status) {
        List<Ticket> tickets;
        log.info("userId :: " + userId);
        //userId로 User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        //status(사용여부)에 따라서 list 에 넣어줌
        if (status) {
            tickets = ticketRepository.findByUserAndStatusAndCanceled(user, true, false);
            log.info("UsedTicket :: " + tickets);
            System.out.println(tickets);
        } else {
            tickets = ticketRepository.findByUserAndStatusAndCanceled(user,false, false);
            log.info("UnusedTicket :: " + tickets);
            System.out.println(tickets);
        }
        //ticket list 출력
        return tickets.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 2. 새로운 ticket 생성
     * @param ticketDTO : 티켓 정보
     * @param userId : 티켓을 구매한 userId
     * @param exhibitId : 구매된 티켓의 exhibitId
     */
    @Transactional
    public void addTicket(TicketDTO ticketDTO, long userId, long exhibitId) {
        //userId로 User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        //exhibitId로 Exhibit 가져오기
        Exhibit exhibit = exhibitRepository.findById(exhibitId)
                .orElseThrow(() -> new IllegalArgumentException("Exhibit not found with id: " + exhibitId));
        // TicketDTO에 userId, exhibitId 설정
        ticketDTO.setUserId(user.getUserId());
        ticketDTO.setExhibitId(exhibit.getExhibitId());
        //TicketDTO -> 엔티티 변환
        Ticket ticket = ticketDTO.toEntity(user, exhibit);
        //Ticket 엔티티 저장
        ticketRepository.save(ticket);
    }

    /**
     * 3. 티켓 취소
     * @param ticketId : 취소할 ticketId
     */
    @Transactional
    public void deleteTicketById(long ticketId){
        //ticketId로 검색
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        //ticket이 존재할 경우 canceled 컬럼값 변경
        optionalTicket.ifPresent(Ticket::setCanceled);
    }

    /**
     * 4. 티켓 사용 여부 변경
     * @param ticketId : 사용할 ticketId
     * @return
     */
    @Transactional
    public boolean useTicketAndCheckIfUsed(long ticketId){
        //ticketId로 검색
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        //티켓이 존재하는지 확인
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            //취소된 티켓인지 확인
            if(!ticket.getCanceled()){
                ticket.setstatus();
                ticketRepository.save(ticket);
                return true;
            } else {
                //티켓이 이미 사용되었거나 취소된 경우
                return false;
            }
        } else {
            //티켓이 존재하지 않는 경우
            return false;
        }
    }

    /**
     * 5. ticketNo 생성
     * @return
     */
    public String generateTicketNumber() {
        // 현재 날짜를 년월일 6자리 형식(YYMMDD)으로 변환
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 6자리 난수 생성을 위한 StringBuilder
        StringBuilder randomPart = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            // 0부터 9까지의 숫자 중 하나를 랜덤하게 선택하여 추가
            int num = ThreadLocalRandom.current().nextInt(0, 10);
            randomPart.append(num);
        }

        // 년월일 부분과 난수 부분을 합쳐 최종 티켓 번호 생성
        return datePart + randomPart;
    }

    /**
     * 6. ticketNo 중복되는지 확인
     * @param ticketNo
     * @return
     */
    public boolean isTicketNumberDuplicate(String ticketNo) {
        return ticketRepository.existsByTicketNo(ticketNo);
    }
}
