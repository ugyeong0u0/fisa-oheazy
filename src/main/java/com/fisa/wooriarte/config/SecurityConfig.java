package com.fisa.wooriarte.config;

import com.fisa.wooriarte.jwt.CustomUserDetailsService;
import com.fisa.wooriarte.jwt.JwtAuthenticationFilter;
import com.fisa.wooriarte.jwt.JwtTokenProvider;
import com.fisa.wooriarte.redis.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final CorsConfig corsConfig;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider,
                          RedisService redisService, CorsConfig corsConfig) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
        this.corsConfig = corsConfig;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customUserDetailsService);
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 모든 요청에 대해 접근을 허용
                )
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection", "1; mode=block")) // XSS 보호 헤더 수동 설정
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("script-src 'self'")
                        )
                        .frameOptions(frame -> frame.deny())
                );

        return httpSecurity.build();
    }
}