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

    //결제 완료 -> 티켓 생성
    @PostMapping("/exhibits/{exhibit-id}/payments/{user-id}")
    public ResponseEntity<String> addTicket(
            @RequestBody TicketDTO ticketDTO,
            @PathVariable(name = "exhibit-id") long exhibitId,
            @PathVariable(name = "user-id") long userId) {
        // 이름, 이메일, 연락처 값이 누락되었는지 확인
        if (ticketDTO.getName() == null || ticketDTO.getEmail() == null || ticketDTO.getPhone() == null) {
            throw new IllegalArgumentException("이름, 이메일, 연락처는 필수 값입니다.");
        }

        try {
            ticketService.addTicket(ticketDTO, userId, exhibitId);
            return ResponseEntity.ok("티켓 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("티켓 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // status에 따라 ticket list 출력 : 관람 예정 / 관람 완료
    @GetMapping("/user/{user-id}/bookings/{status}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserIdAndStatus(
            @PathVariable(name = "user-id") long userId,
            @RequestParam(required = false, defaultValue = "false") boolean status) {

        List<TicketDTO> tickets = ticketService.getTicketsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(tickets);
    }

    // 티켓 취소
    @DeleteMapping("/user/{ticket-id}")
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

    // 티켓 사용 처리 변경
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
