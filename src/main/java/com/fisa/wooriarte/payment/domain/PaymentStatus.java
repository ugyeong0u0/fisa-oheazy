package com.fisa.wooriarte.payment.domain;

public enum PaymentStatus {
    //결제중, 결제완료, 결제취소, 잘못된 결제
    PROGRESSING, FINISH, CANCLED, WRONGPAYMENT, CREATETICKET
}
