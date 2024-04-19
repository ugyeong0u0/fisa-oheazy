package com.fisa.wooriarte.payment.controller;

import com.fisa.wooriarte.payment.dto.PaymentDTO;
import com.fisa.wooriarte.payment.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public String addPayment(@RequestBody Map<String, Long> map) {
        Long exhibitId = map.get("exhibit_id");
        Long amount = map.get("amount");
        PaymentDTO paymentDTO = paymentService.addPayment(exhibitId, amount);
        paymentService.preRegisterPayment(paymentDTO.getOrderNumber(), paymentDTO.getAmount());
        return paymentDTO.getOrderNumber();
    }

    @PostMapping("/payment/verify/{imp_uid}")
    public String verifyPayment(@PathVariable("imp_uid") String impUid) throws IamportResponseException, IOException {
        if(!paymentService.verifyPayment(impUid)) {
            if(paymentService.cancelPayment(impUid)) {
                return "payment fail, cancel complete";
            }
            return "payment fail, cancel fail";
        }
        return "success";
    }

    @PostMapping("/payment/cancel/{order_number}")
    public String cancelPayment(@PathVariable("order_number") String orderNumber) {
        if(paymentService.removePayment(orderNumber))
            return "success";
        return "fail";
    }
}