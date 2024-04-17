package com.fisa.wooriarte.config;

import com.fisa.wooriarte.jwt.JwtAuthenticationFilter;
import com.fisa.wooriarte.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws
            Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/user/jwtlogin").permitAll()
                .requestMatchers("/project-managers/jwtlogin").permitAll()
                .requestMatchers("/space-rentals/jwtlogin").permitAll()
                .requestMatchers("/user/**", "/ticket").hasRole("USER") // 여러 경로에 대해 같은 역할을 지정할 수 있습니다.
                .requestMatchers("/exhibit/**").hasAnyRole("USER", "SPACE_RENTAL", "PROJECT_MANAGER") // 수정: hasAnyRole 사용
                .requestMatchers("/matching/**").hasAnyRole("SPACE_RENTAL", "PROJECT_MANAGER")
                .requestMatchers("/project-managers/**").hasRole("PROJECT_MANAGER")
                .requestMatchers("/project-item/**", "/space-item/**").hasAnyRole("PROJECT_MANAGER", "SPACE_RENTAL") // 수정: hasAnyRole 사용
                .requestMatchers("/space-rentals/**").hasRole("SPACE_RENTAL")
                .requestMatchers("/email", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}

