package com.fisa.wooriarte.refund.controller;

import com.fisa.wooriarte.refund.dto.RefundDto;
import com.fisa.wooriarte.refund.service.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refund")
public class RefundController {

    private final RefundService refundService;
    private final Logger log = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("")
    public ResponseEntity<String> refundTicket(@RequestBody RefundDto refundDto) {
        try {
            log.info("티켓 환불 시도 - 티켓 ID: {}", refundDto.getTicketId());
            boolean refundSuccess = refundService.addRefund(refundDto.getTicketId(), refundDto.getReason());

            if (refundSuccess) {
                log.info("티켓 환불 성공 - 티켓 ID: {}", refundDto.getTicketId());
                return ResponseEntity.ok("환불 성공");
            } else {
                log.warn("티켓 환불 실패 - 티켓 ID: {}", refundDto.getTicketId());
                return ResponseEntity.badRequest().body("환불 실패");
            }
        } catch (Exception e) {
            log.error("티켓 환불 중 예외 발생 - 티켓 ID: {}", refundDto.getTicketId(), e);
            return ResponseEntity.internalServerError().body("예외로 인한 환불 실패");
        }
    }
}