package com.fisa.wooriarte.payment.service;

import com.fisa.wooriarte.payment.dto.PaymentDTO;
import com.fisa.wooriarte.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    public boolean addPayment(PaymentDTO paymentDTO) {
        paymentRepository.save(paymentDTO.toEntity());
        return true;
    }

}
