package com.fisa.wooriarte.payment.controller;

import com.fisa.wooriarte.payment.dto.PaymentDto;
import com.fisa.wooriarte.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public ResponseEntity<?> addPayment(@RequestBody Map<String, Long> payload) {
        try {
            Long exhibitId = payload.get("exhibit_id");
            Long amount = payload.get("amount");
            System.out.println(exhibitId + " " + amount);
            PaymentDto paymentDTO = paymentService.addPayment(exhibitId, amount);
            paymentService.preRegisterPayment(paymentDTO.getOrderNumber(), paymentDTO.getAmount());
            return ResponseEntity.ok(Map.of("merchant_uid", paymentDTO.getOrderNumber(), "amount", paymentDTO.getAmount()));
        } catch(Exception e) {
            log.error("Failed to add project item", e);
            return ResponseEntity.badRequest().body(Map.of("message", "결제에 실패했습니다."));
        }
    }

    @PostMapping("/verifyIamport/{imp_uid}")
    public ResponseEntity<?> verifyPayment(@PathVariable("imp_uid") String impUid) {
        PaymentDto paymentDto = paymentService.verifyPayment(impUid);
        if (paymentDto == null) {
            if (paymentService.cancelPayment(impUid)) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("verified", false, "message", "결제 검증 실패, 결제 취소 완료"));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("verified", false, "message", "결제 검증 실패, 결제 취소 실패"));
            }
        }
        return ResponseEntity.ok(Map.of("verified", true, "paymentId", paymentDto.getPaymentId()));
    }

    @PostMapping("/cancel/{order_number}")
    public String cancelPayment(@PathVariable("order_number") String orderNumber) {
        if(paymentService.removePayment(orderNumber))
            return "success";
        return "fail";
    }
}