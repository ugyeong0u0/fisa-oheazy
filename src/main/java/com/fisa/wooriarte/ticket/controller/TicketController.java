package com.fisa.wooriarte.ticket.controller;

import com.fisa.wooriarte.ticket.dto.TicketDto;
import com.fisa.wooriarte.ticket.dto.TicketResponesDto;
import com.fisa.wooriarte.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RequestMapping("/api/tickets")
@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    /**
     * 1. 티켓 생성(결제 완료 후 생성)
     * @param ticketDto : 티켓 데이터
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> addTicket(@RequestBody TicketDto ticketDto) {
        if(ticketDto.getAmount() <= 0) {
            throw new RuntimeException("티켓 매수는 양수만 가능합니다");
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

            ticketDto.setTicketNo(ticketNo);
            ticketService.addTicket(ticketDto);
            log.info("Ticket creation successful for ticket number: {}", ticketNo);
            return ResponseEntity.ok(Map.of("message", "Ticket successfully created."));
        } catch (Exception e) {
            log.error("Error while creating ticket: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred during ticket creation: " + e.getMessage()));
        }
    }

    /**
     * 2. 관람 예정/관람 완료 ticket list 출력
     * @param userId : 티켓을 보유하고 있는 userId
     * @param status : 티켓이 사용됐는지 안 됐는지 확인하는 Boolean 값 0 : 사용 예정 1 : 사용 완료
     * @return
     */
    @GetMapping("/users/{user-id}/bookings/{status}")
    public ResponseEntity<?> getTicketsByUserIdAndStatus(@PathVariable(name = "user-id") long userId, @PathVariable boolean status) {
        try {
            List<TicketResponesDto> tickets = ticketService.getTicketsByUserIdAndStatus(userId, status);
            log.info("Fetched tickets for user ID: {} with status: {}", userId, status);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            log.error("Error while fetching tickets: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred while fetching tickets."));
        }
    }

    /**
     * 3. 티켓 취소
     * @param ticketId : 취소할 ticketId
     * @return
     */
    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<?> deleteTicket(@PathVariable(name = "ticket-id") long ticketId) {
        try {
            ticketService.deleteTicketById(ticketId);
            log.info("Ticket with ID {} successfully cancelled.", ticketId);
            return ResponseEntity.ok(Map.of("message", "Ticket with ID " + ticketId + " successfully cancelled."));
        } catch (NoSuchElementException e) {
            log.warn("Ticket with ID {} not found.", ticketId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error while cancelling ticket: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred while cancelling ticket."));
        }
    }

    /**
     * 4. 티켓 사용 여부 변경
     * @param ticketId : 사용 여부 변경할 ticketId
     * @return
     */
    @PatchMapping("/{ticket-id}")
    public ResponseEntity<?> useTicket(@PathVariable(name = "ticket-id") long ticketId) {
        try {
            boolean isUsed = ticketService.useTicketAndCheckIfUsed(ticketId);
            if (isUsed) {
                log.info("Status of ticket with ID {} changed to used.", ticketId);
                return ResponseEntity.ok(Map.of("message", "Status of ticket with ID " + ticketId + " changed to used."));
            } else {
                log.warn("Ticket with ID {} does not exist.", ticketId);
                return ResponseEntity.badRequest().body(Map.of("message", "Ticket with ID " + ticketId + " does not exist."));
            }
        } catch (Exception e) {
            log.error("Error while changing ticket status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred while changing ticket status."));

        }
    }

}
