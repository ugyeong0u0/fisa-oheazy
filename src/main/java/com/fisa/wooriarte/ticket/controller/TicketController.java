package com.fisa.wooriarte.ticket.controller;

import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    /**
     * 1. 티켓 생성(결제 완료 후 생성)
     * @param ticketDTO : 티켓 데이터
     * @param exhibitId : 예매될 전시 exhibitId
     * @param userId : 구매자 userId
     * @return
     */
    @PostMapping("/exhibits/{exhibit-id}/payments/{user-id}")
    public ResponseEntity<String> addTicket(@RequestBody TicketDTO ticketDTO, @PathVariable(name = "exhibit-id") long exhibitId, @PathVariable(name = "user-id") long userId) {
        // 이름, 이메일, 연락처 값이 누락되었는지 확인
        if (ticketDTO.getName() == null || ticketDTO.getEmail() == null || ticketDTO.getPhone() == null) {
            throw new IllegalArgumentException("이름, 이메일, 연락처는 필수 값입니다.");
        }
        try {
            String ticketNo;
            boolean isDuplicate;
            do {
                // 티켓 번호 생성
                ticketNo = ticketService.generateTicketNumber();
                // 생성된 티켓 번호가 중복되는지 확인
                isDuplicate = ticketService.isTicketNumberDuplicate(ticketNo);
            } while (isDuplicate); // 중복된 경우 다시 번호 생성

            ticketDTO.setTicketNo(ticketNo);
            ticketService.addTicket(ticketDTO, userId, exhibitId);
            return ResponseEntity.ok("티켓 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("티켓 생성 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 2. 관람 예정/관람 완료 ticket list 출력
     * @param userId : 티켓을 보유하고 있는 userId
     * @param status : 티켓이 사용됐는지 안 됐는지 확인하는 Boolean 값 0 : 사용 예정 1 : 사용 완료
     * @return
     */
    @GetMapping("/user/{user-id}/bookings/{status}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserIdAndStatus(
            @PathVariable(name = "user-id") long userId,
            @PathVariable boolean status) {

        List<TicketDTO> tickets = ticketService.getTicketsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(tickets);
    }

    /**
     * 3. 티켓 취소
     * @param ticketId : 취소할 ticketId
     * @return
     */
    @DeleteMapping("/user/ticket/{ticket-id}")
    public ResponseEntity<String> deleteTicket(@PathVariable(name = "ticket-id") long ticketId) {
        try {
            ticketService.deleteTicketById(ticketId);
            return ResponseEntity.ok("취소된 티켓 ID " + ticketId + "입니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("티켓 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 4. 티켓 사용 여부 변경
     * @param ticketId : 사용 여부 변경할 ticketId
     * @return
     */
    @PatchMapping("/{ticket-id}")
    public ResponseEntity<String> useTicket(@PathVariable(name = "ticket-id") long ticketId) {
        boolean isUsed = ticketService.useTicketAndCheckIfUsed(ticketId);
        if (isUsed) {
            return ResponseEntity.ok("Ticket with ID " + ticketId + " status changed.");
        } else {
            return ResponseEntity.badRequest().body("Ticket with ID " + ticketId + " does not exist.");
        }
    }

}
