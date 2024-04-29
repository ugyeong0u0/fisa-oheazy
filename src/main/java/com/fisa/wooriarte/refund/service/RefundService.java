package com.fisa.wooriarte.refund.service;

import com.fisa.wooriarte.refund.domain.Refund;
import com.fisa.wooriarte.refund.dto.RefundDto;
import com.fisa.wooriarte.refund.repository.RefundRepository;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final TicketRepository ticketRepository;

    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;

    private IamportClient iamportClient;

    @Autowired
    public RefundService(RefundRepository refundRepository, TicketRepository ticketRepository) {
        this.refundRepository = refundRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    @Transactional
    public boolean addRefund(Long ticketId, String reason) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("해당 티켓 없음"));
        if(ticket.getCanceled()) {
            throw new RuntimeException("이미 취소된 티켓입니다.");
        }
        try {
            IamportResponse<Payment> iamPayment = iamportClient.cancelPaymentByImpUid(new CancelData(ticket.getPayment().getApprovalNumber(), true));
            if(iamPayment.getCode() != 0) {
                throw new RuntimeException("환불에 실패했습니다.");
            }
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
        refundRepository.save(Refund.builder()
                .ticket(ticket)
                .amount(ticket.getAmount())
                .reason(reason)
                .build());
        ticket.setCanceled();
        ticketRepository.save(ticket);
        return true;
    }
}
