package com.fisa.wooriarte.payment.controller;

import com.fisa.wooriarte.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PaymentController {

    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;

    private final PaymentService paymentService;

    private IamportClient iamportClient;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    @PostMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        Payment payment = iamportClient.paymentByImpUid(imp_uid).getResponse();
        
        return iamportClient.paymentByImpUid(imp_uid);
    }
}