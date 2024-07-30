package com.fisa.wooriarte.refund.controller;

import com.fisa.wooriarte.projectmanager.controller.ProjectManagerController;
import com.fisa.wooriarte.refund.dto.RefundDto;
import com.fisa.wooriarte.refund.service.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/refund")
public class RefundController {

    private final RefundService refundService;
    private final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);
    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("")
    public ResponseEntity<?> refundTicket(@RequestBody RefundDto refundDto) {
        try {
            log.info("Trying to refund ticket");
            if(refundService.addRefund(refundDto.getTicketId(), refundDto.getReason())) {
                return ResponseEntity.ok(Map.of("message", "refund success"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "refund failed"));
            }
        } catch (Exception e) {
            log.info("Exception occurred while refunding ticket {}", refundDto.getTicketId(), e);
            return ResponseEntity.badRequest().body(Map.of("message", "refund fail by exception"));
        }
    }
}
