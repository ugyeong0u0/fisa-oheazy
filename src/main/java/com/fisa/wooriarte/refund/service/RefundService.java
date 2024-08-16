package com.fisa.wooriarte.refund.service;

import com.fisa.wooriarte.refund.domain.Refund;
import com.fisa.wooriarte.refund.repository.RefundRepository;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(RefundService.class);

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

    /**
     * 환불 처리 메서드
     * @param ticketId 티켓 ID
     * @param reason 환불 사유
     * @return 환불 성공 여부
     */
    @Transactional
    public boolean addRefund(Long ticketId, String reason) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("해당 티켓을 찾을 수 없습니다."));

        if (ticket.getCanceled()) {
            log.warn("이미 취소된 티켓입니다. - 티켓 ID: {}", ticketId);
            throw new IllegalStateException("이미 취소된 티켓입니다.");
        }

        try {
            IamportResponse<Payment> iamPayment = iamportClient.cancelPaymentByImpUid(new CancelData(ticket.getPayment().getApprovalNumber(), true));

            if (iamPayment.getCode() != 0) {
                log.error("환불 요청 실패 - Iamport API 응답 코드: {}, 티켓 ID: {}", iamPayment.getCode(), ticketId);
                throw new RuntimeException("환불에 실패했습니다.");
            }

            log.info("환불 성공 - 티켓 ID: {}", ticketId);
        } catch (IamportResponseException | IOException e) {
            log.error("환불 처리 중 예외 발생 - 티켓 ID: {}", ticketId, e);
            throw new RuntimeException("환불 처리 중 예외가 발생했습니다.", e);
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