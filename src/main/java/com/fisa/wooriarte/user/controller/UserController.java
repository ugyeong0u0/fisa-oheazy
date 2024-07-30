package com.fisa.wooriarte.user.controller;

import com.fisa.wooriarte.ErrorMessage;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.user.dto.UserDto;
import com.fisa.wooriarte.user.dto.request.UserInfoRequestDto;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import com.fisa.wooriarte.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    ErrorMessage errorMessage = new ErrorMessage(); // 에러 처리시 사용하는 클래스

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 유저 회원가입
     * 회원 삭제 소프트딜리트라 email 중복확인때 isDeleted도 봐야함
     * @param userDto
     * @return
     */
    // 이메일 중복 체크를 통해 중복회원 거르기
    @PostMapping("")
    //<?> : 제네릭 타입, 모든 종류의 응답 본문 반환할지 나타낸다.
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto)  {
        try {
            boolean result = userService.addUser(userDto);
            if (result) {
                return ResponseEntity.ok().body("회원가입 성공");
            } else {
                // 이 경우에는 일반적으로 도달하지 않음, 예외를 통해 처리 예정이므로 따로 처리 없음
                log.error("회원가입 실패: 알 수 없는 오류");
                return ResponseEntity.badRequest().build(); // 직접적으로 에러 메시지를 클라이언트에 전달하지 않음
            }
        } catch (IllegalStateException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build(); // 에러 메시지 노출 시키지 않기 위함
        } catch (Exception e) {
            log.error("서버 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 에러 메시지 노출 시키지 않기 위함
        }
    }

    /**
     * 유저 로그인
     *
     * @param loginInfo
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginInfo) {
        String id = loginInfo.get("id");
        String pwd = loginInfo.get("pwd");

        try {
            UserDto userDto = userService.loginUser(id, pwd);
            return ResponseEntity.ok(userDto.getUserId().toString());

        } catch (IllegalAccessException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("로그인 중 에러 발생: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 서버 내부 오류 처리
        }

    }

    @PostMapping("/jwtlogin")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        try {
            String id = userLoginRequestDTO.getId();
            String pwd = userLoginRequestDTO.getPwd();

            JwtToken jwtToken = userService.login(id, pwd);

            log.info("로그인 성공 - 사용자 ID: {}", id);
            return ResponseEntity.ok(jwtToken); // 성공하면 JWT 토큰 반환
        } catch (AuthenticationException e) {
            // AuthenticationService에서 Custom Exception을 정의했다고 가정
            log.error("로그인 실패 - 사용자 ID: {}, 이유: {}", userLoginRequestDTO.getId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage()); // 실패하면 UNAUTHORIZED와 실패 메시지 반환
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("서버 내부 에러 발생 - 이유: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류: " + e.getMessage()); // 내부 서버 오류 발생시
        }
    }

    /**
     * 유저 비밀번호 검증
     *
     * @return
     */
    // 유저 마이페이지 수정하기 위한 -> 비밀번호 검증
    @PostMapping("/{user-id}/verify-pwd")
    public ResponseEntity<?> verifyPassword(@PathVariable(name = "user-id") Long userId, @RequestBody UserDto userDto)  {
        try {
            if (userService.verifyPassword(userId, userDto.getPwd())) {
                return new ResponseEntity<>("비밀번호 인증 성공", HttpStatus.OK);
            } else {
                log.warn("비밀번호 틀림 - 사용자 ID: {}", userId);
                return new ResponseEntity<>("비밀번호 틀림", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("서버 내부 오류: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류 발생");
        }

    }

    // 유저 개인 정보 조회
    @GetMapping("/{user-id}/info")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "user-id") Long userId){
        try {
            UserDto userInfo = userService.getUserInfo(userId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            log.error("유저 정보 조회 실패 - 사용자 ID: {}: ", userId, e);
            return ResponseEntity.badRequest().body("정보 조회 중 오류 발생");
        }
    }

    /**
     * 유저 개인 정보 수정
     * @param userId
     * @param userInfoRequestDto
     * @return
     * @throws Exception
     */
    @PutMapping("/{user-id}/info")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name = "user-id") Long userId, @RequestBody UserInfoRequestDto userInfoRequestDto) {
        try {
            Boolean result = userService.updateMyUser(userId, userInfoRequestDto);
            if (result) return ResponseEntity.ok("수정 성공");
            else {
                log.warn("사용자 정보 수정 실패: 사용자 ID = {}", userId);
                return ResponseEntity.badRequest().body("수정 실패");
            }
        } catch (IllegalStateException e) {
            log.error("예외 발생", e);
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            log.error("서버 내부 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    /**
     * 유저 아이디 찾기
     * @param userDto
     * @return
     */
    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserDto userDto) {
        String email = userDto.getEmail();

        try {
            String userId = userService.findUserId(email);
            return ResponseEntity.ok().body(userId);
        } catch (NoSuchElementException e) {
            log.warn("일치하는 사용자 아이디 찾기 실패: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 이메일로 등록된 아이디가 없습니다.");
        } catch (Exception e) {
            log.error("서버 내부 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    //클라이언트가 저송한
    //비밀번호 찾기
    @PostMapping("/set-pw")
    public ResponseEntity<?> setUserPw(@RequestBody UserDto userDTO ) {
        try {
            boolean result = userService.findUserPw(userDTO);
            if(result) {
                return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 완료"));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "입력한 아이디, 이름과 이메일을 다시 확인해주세요."));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","서버 오류 발생"));
    }

    /**
     * 유저 삭제하기
     * @param userId
     * @return
     */
    @DeleteMapping("/{user-id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user-id") Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body("유저 삭제 성공");
        } catch (NoSuchElementException e) {
            log.error("유저 삭제 실패 - 유저를 찾을 수 없습니다. 유저 ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 유저를 찾을 수 없습니다.");
        } catch (DataIntegrityViolationException e) {
            log.error("유저 삭제 중 데이터 무결성 위배 발생. 유저 ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("데이터 무결성 문제로 삭제할 수 없습니다.");
        } catch (Exception e) {
            log.error("서버 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }



}


