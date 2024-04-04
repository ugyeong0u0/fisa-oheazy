package com.fisa.wooriarte.ticket.controller;

import com.fisa.wooriarte.ticket.dto.TicketDTO;
import com.fisa.wooriarte.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    //결제 완료 -> 티켓 생성
    @PostMapping("/exhibits/{userId}/bookings/payments")
    public String addTicket(@RequestBody TicketDTO ticketDTO, @PathVariable("userId") long userId) {
        log.info("addTicket :: " + String.valueOf(ticketDTO.toString()));
        System.out.println("userID :: " + userId);
        try {
            ticketService.createTicket(ticketDTO, userId);
            System.out.println("티켓 생성 완료");
            return "success";
        } catch (Exception e) {
            System.err.println("티켓 생성 중 오류 발생: " + e.getMessage());
            return "Failed to create ticket: ";
        }
    }

    // status에 따라 ticket list 출력 : 관람 예정 / 관람 완료
    @GetMapping("/user/bookings/{userId}/{status}")
    public List<TicketDTO> getTicketsByUserIdAndStatus(@PathVariable("userId") long userId, @PathVariable("status") boolean status) {
        return ticketService.getTicketsByUserIdAndStatus(userId, status);
    }

    // 티켓 취소
    @DeleteMapping("/user/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable long ticketId) {
        ticketService.deleteTicketById(ticketId);
        return ResponseEntity.ok("Ticket with ID " + ticketId + " has been canceled.");
    }

    // 티켓 사용 처리 변경
    @PatchMapping("/user/{ticketId}")
    public ResponseEntity<String> useTicket(@PathVariable long ticketId) {
        boolean isUsed = ticketService.useTicketAndCheckIfUsed(ticketId);
        if (isUsed) {
            return ResponseEntity.ok("Ticket with ID " + ticketId + " status changed.");
        } else {
            return ResponseEntity.badRequest().body("Ticket with ID " + ticketId + " does not exist.");
        }
    }
}
