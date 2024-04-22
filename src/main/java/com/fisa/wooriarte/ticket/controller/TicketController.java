package com.fisa.wooriarte.ticket.controller;

import com.fisa.wooriarte.ticket.dto.TicketDto;
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
@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    //결제 완료 -> 티켓 생성
    @PostMapping("/exhibits/{exhibit-id}/payments/{user-id}")
    public ResponseEntity<?> addTicket(@RequestBody TicketDto ticketDTO, @PathVariable(name = "exhibit-id") long exhibitId, @PathVariable(name = "user-id") long userId) {
        if (ticketDTO.getName() == null || ticketDTO.getEmail() == null || ticketDTO.getPhone() == null) {
            log.warn("Ticket creation failed due to missing information");
            throw new IllegalArgumentException("Name, email, and phone number are required.");
        }

        try {
            String ticketNo = ticketService.generateTicketNumber();
            ticketDTO.setTicketNo(ticketNo);
            ticketService.addTicket(ticketDTO, userId, exhibitId);
            log.info("Ticket creation successful for user ID: {}, Exhibit ID: {}", userId, exhibitId);
            return ResponseEntity.ok(Map.of("message", "Ticket successfully created."));
        } catch (Exception e) {
            log.error("Error while creating ticket: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred during ticket creation: " + e.getMessage()));
        }
    }

    // status에 따라 ticket list 출력 : 관람 예정 / 관람 완료
    @GetMapping("/user/{user-id}/bookings/{status}")
    public ResponseEntity<?> getTicketsByUserIdAndStatus(@PathVariable(name = "user-id") long userId, @PathVariable boolean status) {
        try {
            List<TicketDto> tickets = ticketService.getTicketsByUserIdAndStatus(userId, status);
            log.info("Fetched tickets for user ID: {} with status: {}", userId, status);
            return ResponseEntity.ok(Map.of("message", "Tickets fetched successfully.", "tickets", tickets));
        } catch (Exception e) {
            log.error("Error while fetching tickets: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error occurred while fetching tickets."));
        }
    }

    // 티켓 취소
    @DeleteMapping("/user/ticket/{ticket-id}")
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

    // 티켓 사용 처리 변경
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
