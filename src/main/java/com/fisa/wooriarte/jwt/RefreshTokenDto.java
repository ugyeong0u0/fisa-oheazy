package com.fisa.wooriarte.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Data
public class RefreshTokenDto {
    private String refreshToken;
}
