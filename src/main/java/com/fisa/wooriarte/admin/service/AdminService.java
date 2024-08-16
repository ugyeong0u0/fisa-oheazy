package com.fisa.wooriarte.admin.service;

import com.fisa.wooriarte.admin.domain.Admin;
import com.fisa.wooriarte.admin.dto.AdminDto;
import com.fisa.wooriarte.admin.repository.AdminRepository;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 새로운 관리자 계정 추가
     * @param adminDto 관리자 정보 DTO
     * @return 추가 성공 여부
     */
    @Transactional
    public Boolean addAdmin(AdminDto adminDto) {
        Admin admin = adminDto.toEntity();
        admin.addRole("ADMIN");
        admin.setPwd(passwordEncoder.encode(adminDto.getPwd()));
        adminRepository.save(admin);
        return true;
    }

    /**
     * 관리자 로그인 및 JWT 토큰을 생성
     * @param id 관리자 아이디
     * @param pwd 관리자 비밀번호
     * @return 생성된 JWT 토큰
     */
    public JwtToken login(String id, String pwd) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
            // 사용자 인증
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 인증 정보를 기반으로 JWT 토큰 생성
            return jwtTokenProvider.generateToken(authentication);
        } catch (Exception e) {
            throw e;
        }
    }
}