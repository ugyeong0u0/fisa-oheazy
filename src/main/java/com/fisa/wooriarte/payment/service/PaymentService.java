package com.fisa.wooriarte.payment.service;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.exhibit.repository.ExhibitRepository;
import com.fisa.wooriarte.payment.domain.Payment;
import com.fisa.wooriarte.payment.domain.PaymentStatus;
import com.fisa.wooriarte.payment.dto.PaymentDTO;
import com.fisa.wooriarte.payment.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {

    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    private final PaymentRepository paymentRepository;
    private final ExhibitRepository exhibitRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ExhibitRepository exhibitRepository) {
        this.paymentRepository = paymentRepository;
        this.exhibitRepository = exhibitRepository;
    }

    @Transactional
    public PaymentDTO addPayment(Long exhibitId, Long amount) {
        Exhibit exhibit = exhibitRepository.findById(exhibitId).orElseThrow(() -> new NoSuchElementException("해당 전시가 없습니다."));
        Payment payment = Payment.builder()
                .orderNumber(UUID.randomUUID().toString())
                .amount(exhibit.getPrice() * amount)
                .status(PaymentStatus.PROGRESSING)
                .build();
        paymentRepository.save(payment);
        return PaymentDTO.fromEntity(payment);
    }

    public void preRegisterPayment(String orderNumber, Long amount) {
        IamportResponse<Prepare> iamportResponse = null;
        try {
            iamportResponse = iamportClient.postPrepare( new PrepareData(orderNumber, new BigDecimal(amount)));
        } catch (IOException | IamportResponseException e) {
            throw new RuntimeException(e);
        }
        if(iamportResponse.getCode() != 0) {
            throw new RuntimeException(iamportResponse.getMessage());
        }
    }

    @Transactional
    public boolean cancelPayment(String impUid) {
        Payment payment = paymentRepository.findByApprovalNumber(impUid)
                .orElseThrow(() -> new NoSuchElementException("승인번호 오류"));
        IamportResponse<com.siot.IamportRestClient.response.Payment> iamPayment = null;
        try {
            iamPayment = iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
        if(iamPayment.getCode() != 0) {
            throw new RuntimeException(iamPayment.getMessage());
        }
        payment.setStatus(PaymentStatus.CANCLED);
        paymentRepository.save(payment);
        return true;
    }

    @Transactional
    public boolean verifyPayment(String impUid) {
        com.siot.IamportRestClient.response.Payment iamPayment = null;
        try {
            iamPayment = iamportClient.paymentByImpUid(impUid).getResponse();
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
        Payment payment = paymentRepository.findByOrderNumber(iamPayment.getMerchantUid())
                .orElseThrow(() -> new NoSuchElementException("주문번호 오류"));
        if(!iamPayment.getStatus().equals("paid") || payment.getStatus() != PaymentStatus.PROGRESSING || payment.getAmount() != iamPayment.getAmount().longValue()) {
            payment.updatePayment(iamPayment.getPayMethod(), impUid, PaymentStatus.WRONGPAYMENT);
            paymentRepository.save(payment);
            return false;
        }
        payment.updatePayment(iamPayment.getPayMethod(), impUid, PaymentStatus.FINISH);
        paymentRepository.save(payment);
        return true;
    }

    @Transactional
    public boolean removePayment(String orderNumber) {
        Payment payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NoSuchElementException("주문번호 오류"));
        payment.setStatus(PaymentStatus.CANCLED);
        paymentRepository.save(payment);
        return true;
    }
}
