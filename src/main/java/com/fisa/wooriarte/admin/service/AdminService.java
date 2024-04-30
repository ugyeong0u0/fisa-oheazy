package com.fisa.wooriarte.admin.service;

import com.fisa.wooriarte.admin.domain.Admin;
import com.fisa.wooriarte.admin.dto.AdminDto;
import com.fisa.wooriarte.admin.repository.AdminRepository;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.jwt.JwtTokenProvider;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    public Boolean addAdmin(AdminDto adminDto) {

        Admin admin = adminDto.toEntity();
        admin.addRole("ADMIN");
        admin.setPwd(passwordEncoder.encode(adminDto.getPwd()));
        adminRepository.save(admin);

        return true;
    }

    public JwtToken login(String id, String pwd) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행

        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 로깅
            throw e; // 필요한 경우, 예외를 다시 던져 처리할 수 있습니다.
        }
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }
}
