package com.fisa.wooriarte.user.dto;

import com.fisa.wooriarte.jwt.JwtToken;

public class UserResponseDto {
    private String message; // 'string' 대신 더 명확한 이름 사용
    private JwtToken jwtToken;

    public UserResponseDto(String message) {
        this.message = message;
    }

    public UserResponseDto(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserResponseDto(String message, JwtToken jwtToken) {
        this.message = message;
        this.jwtToken = jwtToken;
    }

    public String getMessage() {
        return message;
    }

    public JwtToken getJwtToken() {
        return jwtToken;
    }
}