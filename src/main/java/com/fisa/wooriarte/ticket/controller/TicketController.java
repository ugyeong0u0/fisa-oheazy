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
    @PostMapping("/exhibits/{id}/payments/{userid}")
    public String addTicket(@RequestBody TicketDTO ticketDTO, @PathVariable long userid, @PathVariable long id) {
        log.info("addTicket :: " + String.valueOf(ticketDTO.toString()));
        System.out.println("userID :: " + userid);

        // 이름, 이메일, 연락처 값이 누락되었는지 확인
        if (ticketDTO.getName() == null || ticketDTO.getEmail() == null || ticketDTO.getPhone() == null) {
            throw new IllegalArgumentException("이름, 이메일, 연락처는 필수 값입니다.");
        }

        try {
            ticketService.addTicket(ticketDTO, userid, id);
            System.out.println("티켓 생성 완료");
            return "success";
        } catch (Exception e) {
            System.err.println("티켓 생성 중 오류 발생: " + e.getMessage());
            return "Failed to create ticket: ";
        }
    }

    // status에 따라 ticket list 출력 : 관람 예정 / 관람 완료
    @GetMapping("/user/bookings/{id}/{status}")
    public List<TicketDTO> getTicketsByUserIdAndStatus(@PathVariable long id, @PathVariable("status") boolean status) {
        return ticketService.getTicketsByUserIdAndStatus(id, status);
    }

    // 티켓 취소
    @DeleteMapping("/user/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable long ticketId) {
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
