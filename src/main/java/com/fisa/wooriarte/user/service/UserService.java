package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.jwt.JwtTokenProvider;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDto;
import com.fisa.wooriarte.user.dto.request.UserInfoRequestDto;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입을 처리합니다.
     * @param userDto 회원가입할 유저의 정보
     * @return 회원가입 성공 여부
     */
    @Transactional
    public boolean addUser(UserDto userDto) {
        User userEntity = userDto.toEntity();
        Optional<User> userEmail = userRepository.findByEmail(userEntity.getEmail());

        if (userEmail.isPresent() && !userEmail.get().getIsDeleted()) {
            throw new IllegalStateException("회원가입 불가능(이메일 중복)");
        }

        Optional<User> userId = userRepository.findById(userEntity.getId());

        if (userId.isPresent() && !userEmail.get().getIsDeleted()) {
            throw new IllegalStateException("회원가입 불가능 (아이디 중복)");
        }

        userEntity.addRole("USER");
        if (userDto.getPwd() != null) {
            userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
        }
        userRepository.save(userEntity);
        return true;
    }

    /**
     * 비밀번호를 검증합니다.
     * @param id 유저 ID
     * @param password 입력된 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean verifyPassword(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id 유저가 없습니다."));
        return passwordEncoder.matches(password, user.getPwd());
    }

    /**
     * 유저의 개인 정보를 조회합니다.
     * @param userId 조회할 유저의 ID
     * @return 유저의 개인 정보
     */
    public UserDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 id 유저가 없습니다."));
        return UserDto.fromEntity(user);
    }

    /**
     * 유저 로그인을 처리합니다.
     * @param id 유저 ID
     * @param pwd 유저 비밀번호
     * @return 로그인한 유저의 정보
     * @throws IllegalAccessException 로그인 실패 시 예외 발생
     */
    public UserDto loginUser(String id, String pwd) throws IllegalAccessException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent() && passwordEncoder.matches(pwd, optionalUser.get().getPwd())) {
            return UserDto.fromEntity(optionalUser.get());
        } else {
            throw new IllegalAccessException("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    /**
     * JWT 기반의 유저 로그인을 처리합니다.
     * @param id 유저 ID
     * @param pwd 유저 비밀번호
     * @return 생성된 JWT 토큰
     */
    @Transactional
    public JwtToken login(String id, String pwd) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("로그인 실패: 인증 오류가 발생했습니다.", e);
        }
    }

    /**
     * 유저의 개인 정보를 수정합니다.
     * @param id 수정할 유저의 ID
     * @param userInfoRequestDto 수정할 유저의 정보
     * @return 수정 성공 여부
     */
    @Transactional
    public Boolean updateMyUser(Long id, UserInfoRequestDto userInfoRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음"));
        user.updateUser(userInfoRequestDto);
        userRepository.save(user);
        return true;
    }

    /**
     * 유저 아이디를 이메일로 찾습니다.
     * @param email 유저의 이메일
     * @return 찾은 유저 ID
     */
    public String findUserId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("유저 아이디를 찾을 수 없습니다."));
        return user.getId();
    }

    /**
     * 유저 비밀번호를 찾고 설정합니다.
     * @param userDto 유저 정보
     * @return 비밀번호 재설정 성공 여부
     */
    @Transactional
    public boolean findUserPw(UserDto userDto) {
        User user = userRepository.findByIdAndNameAndEmail(userDto.getId(), userDto.getName(), userDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("유저 비밀번호를 찾을 수 없습니다."));
        user.setPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(user);
        return true;
    }

    /**
     * 유저를 삭제합니다.
     * @param userId 삭제할 유저의 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deleteUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("없는 유저입니다."));
            if (user.getIsDeleted()) {
                throw new DataIntegrityViolationException("이미 삭제된 유저입니다.");
            }
            user.setIsDeleted();
            userRepository.save(user);
            return true;
        } catch (NoSuchElementException | DataIntegrityViolationException e) {
            throw e; // 특정 예외는 그대로 던짐
        } catch (Exception e) {
            throw new RuntimeException("유저 삭제 중 오류가 발생했습니다.", e); // 일반적인 예외는 RuntimeException으로 래핑하여 던짐
        }
    }
}