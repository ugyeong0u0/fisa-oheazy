package com.fisa.wooriarte.user.dto.request;

import lombok.Getter;
import lombok.Setter;


// 유저 마이페이지에서 유저가 수정한 값을 받기위한 클래스
@Setter
@Getter
public class UserInfoRequestDto {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private Boolean isDeleted;
}
