package com.fisa.wooriarte.jwt;


import com.fisa.wooriarte.redis.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;

    private final Key key;
    private final RedisService redisService;
    private CustomUserDetailsService customUserDetailsService;

    // application.yml에서 secret 값 가져와서 key에 저장
    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisService redisService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisService = redisService;
    }

    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication) {
        // 사용자 id 가져오기
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String id = userDetails.getUsername();

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("userId", id)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("userId", id)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisService.setValues(authentication.getName(), refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return JwtToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .id(id)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        System.out.println(token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JwtToken refreshToken(String refreshToken) {
        System.out.println(refreshToken);
        // Refresh 토큰 유효성 검사
        if (!validateRefreshToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh 토큰입니다.");
        }
        // Redis에서 Refresh 토큰 검증
        String id = getIdFromToken(refreshToken);
        String redisRefreshToken = redisService.getData(id);
        System.out.println(id);
        System.out.println(redisRefreshToken);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh 토큰이 만료되었거나 일치하지 않습니다.");
        }

        // 새 Access 토큰 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(id);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return generateToken(authentication);
    }

    public String getIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String generateAccessToken(String userId) {
        // 사용자 ID로부터 UserDetails 객체를 조회
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        // UserDetails를 바탕으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Authentication 객체를 사용하여 토큰 생성
        JwtToken jwtToken = generateToken(authentication);

        // 생성된 액세스 토큰 반환
        return jwtToken.getAccessToken();
    }

}
