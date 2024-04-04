package com.fisa.wooriarte.find.dto.request;

import lombok.Getter;
import lombok.Setter;

// 공간대여자 비밀번호 찾을 때 프론트에서 들어오는 요청값
// 따로 둔 이유 : check로 인증번호 인증완료됐는지 프론트에서 값 받는다는 전제 하에 작성함
@Getter
@Setter
public class FindBusinessPassRequest {
    private String id;
    private long businessNumber;
    private String email;
    private Boolean check; // 인증번호 완료 됐는지 체크 후 아이디 찾기
}
