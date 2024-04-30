package com.fisa.wooriarte.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Long getUserId();
}
