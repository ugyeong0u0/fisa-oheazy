package com.fisa.wooriarte.user.controller;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.user.dto.UserDto;
import com.fisa.wooriarte.user.dto.request.UserInfoRequestDto;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import com.fisa.wooriarte.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 유저 회원가입
    @PostMapping("")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        try {
            boolean isSuccess = userService.addUser(userDto);
            log.info("유저 회원가입 성공 - Email: {}, ID: {}", userDto.getEmail(), userDto.getId());
            return isSuccess ? ResponseEntity.ok("회원가입 성공") : ResponseEntity.badRequest().body("회원가입 실패: 중복된 이메일 또는 아이디가 존재합니다.");
        } catch (Exception e) {
            log.error("유저 회원가입 실패 - Email: {}, ID: {}", userDto.getEmail(), userDto.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: 서버 오류가 발생했습니다.");
        }
    }

    // 유저 JWT 로그인
    @PostMapping("/jwtlogin")
    public ResponseEntity<JwtToken> login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        try {
            JwtToken jwtToken = userService.login(userLoginRequestDTO.getId(), userLoginRequestDTO.getPwd());
            log.info("JWT 로그인 성공 - 사용자 ID: {}", userLoginRequestDTO.getId());
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error("JWT 로그인 실패 - 사용자 ID: {}", userLoginRequestDTO.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 유저 개인 정보 조회
    @GetMapping("/{user-id}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable(name = "user-id") Long userId) {
        try {
            UserDto userInfo = userService.getUserInfo(userId);
            log.info("유저 정보 조회 성공 - ID: {}", userId);
            return ResponseEntity.ok(userInfo);
        } catch (NoSuchElementException e) {
            log.error("유저 정보 조회 실패 - ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // 유저 개인 정보 수정
    @PutMapping("/{user-id}")
    public ResponseEntity<String> updateUserInfo(@PathVariable(name = "user-id") Long userId,
                                                 @RequestBody UserInfoRequestDto userInfoRequestDto) {
        boolean result = userService.updateMyUser(userId, userInfoRequestDto);
        log.info("유저 정보 수정 성공 - ID: {}", userId);
        return result ? ResponseEntity.ok("수정 성공") : ResponseEntity.badRequest().body("수정 실패");
    }

    // 유저 삭제하기
    @DeleteMapping("/{user-id}")
    public ResponseEntity<String> deleteUser(@PathVariable("user-id") Long userId) {
        boolean result = userService.deleteUser(userId);
        log.info("유저 삭제 처리 - ID: {}", userId);
        return result ? ResponseEntity.ok("유저 삭제 성공") : ResponseEntity.badRequest().body("유저 삭제 실패");
    }

    // 유저 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<String> findUserId(@RequestBody UserDto userDto) {
        try {
            String userId = userService.findUserId(userDto.getEmail());
            log.info("유저 아이디 찾기 성공 - Email: {}, ID: {}", userDto.getEmail(), userId);
            return ResponseEntity.ok().body(userId);
        } catch (NoSuchElementException e) {
            log.error("유저 아이디 찾기 실패 - Email: {}", userDto.getEmail(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 아이디를 찾을 수 없습니다.");
        }
    }

    // 유저 비밀번호 찾기
    @PostMapping("/set-pw")
    public ResponseEntity<String> setUserPw(@RequestBody UserDto userDTO) {
        boolean result = userService.findUserPw(userDTO);
        log.info("유저 비밀번호 재설정 성공 - ID: {}", userDTO.getId());
        return result ? ResponseEntity.ok("비밀번호 재설정 완료") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 실패");
    }

    // 유저 비밀번호 검증
    @PostMapping("/{user-id}/verify-pwd")
    public ResponseEntity<String> verifyPassword(@PathVariable(name = "user-id") Long userId, @RequestBody UserDto userDto) {
        boolean result = userService.verifyPassword(userId, userDto.getPwd());
        log.info("비밀번호 검증 성공 - ID: {}", userId);
        return result ? ResponseEntity.ok("비밀번호 인증 성공") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 틀림");
    }

    // 유저 로그인 - jwt 로그인으로 대체. 사용x
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginInfo) {
        try {
            UserDto userDto = userService.loginUser(loginInfo.get("id"), loginInfo.get("pwd"));
            log.info("유저 로그인 성공 - ID: {}", loginInfo.get("id"));
            return ResponseEntity.ok(userDto.getUserId().toString());
        } catch (IllegalAccessException e) {
            log.error("유저 로그인 실패 - ID: {}", loginInfo.get("id"));
            return ResponseEntity.badRequest().body("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}