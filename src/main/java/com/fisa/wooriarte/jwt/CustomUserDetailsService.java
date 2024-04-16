package com.fisa.wooriarte.jwt;

import com.fisa.wooriarte.user.repository.UserRepository;
import com.fisa.wooriarte.user.domain.User; // 이 부분을 추가하세요
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findUserById(id)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(com.fisa.wooriarte.user.domain.User user) {
        return org.springframework.security.core.userdetails.User.builder() // 충돌을 피하기 위해 전체 패키지 명시
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles().toArray(new String[0])) // 이 부분은 user 객체의 구조에 따라 조정이 필요할 수 있습니다.
                .build();
    }
}
