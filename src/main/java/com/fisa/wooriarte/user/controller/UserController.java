package com.fisa.wooriarte.user.controller;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.user.dto.UserDto;
import com.fisa.wooriarte.user.dto.request.UserInfoRequestDto;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import com.fisa.wooriarte.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    ErrorMessage errorMessage = new ErrorMessage(); // 에러 처리시 사용하는 클래스

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 유저 회원가입
     * 회원 삭제 소프트딜리트라 email 중복확인때 isDeleted도 봐야함
     * @param userDTO
     * @return
     */
    // 이메일 중복 체크를 통해 중복회원 거르기
    @PostMapping("")
    //<?> : 제네릭 타입, 모든 종류의 응답 본문 반환할지 나타낸다.
    public ResponseEntity<?> addUser(@RequestBody UserDto userDTO)  {
        try {
            boolean result = userService.addUser(userDTO);
            if (result) { //result 값이 true -> 회원가입 성공
                return ResponseEntity.ok().body("회원가입 성공");
            } else { //result 값이 False -> 회원가입 실패
                // 일반적으로 여기에 도달하지 않음, 예외 처리를 통해 다룰 예정
                errorMessage.setMsg("회원가입 실패: 알 수 없는 오류");
                errorMessage.setErrorCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(errorMessage);
            }
        }catch (IllegalStateException e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.BAD_REQUEST.value()); // service에서 던지는
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            errorMessage.setMsg("서버 오류 발생: " + e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
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
        UserDTO userDto = null;

        try {
            userDto = userService.loginUser(id, pwd);
            return new ResponseEntity<>(userDto.getUserId().toString(), HttpStatus.OK);

        } catch (IllegalAccessException e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            errorMessage.setMsg("로그인 중 에러 발생: " + e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // 서버 내부 에러 처리
        }

    }

    @PostMapping("/jwtlogin")
    public JwtToken login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        String id = userLoginRequestDTO.getId();
        String pwd = userLoginRequestDTO.getPwd();
        return userService.login(id, pwd);
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
                errorMessage.setMsg("비밀번호 틀림");
                errorMessage.setErrorCode(HttpStatus.UNAUTHORIZED.value());
                return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            errorMessage.setMsg("서버 내부 오류: " + e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    // 유저 개인 정보 조회
    @GetMapping("/{user-id}/info")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "user-id") Long userId){
        try {

            UserDto userInfo = userService.getMyUser(userId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * 유저 개인 정보 수정
     * @param userId
     * @param userInfoRequest
     * @return
     * @throws Exception
     */
    @PutMapping("/{user-id}/info")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name = "user-id") Long userId, @RequestBody UserInfoRequestDto userInfoRequestDto) throws Exception {
        try {
            Boolean result = userService.updateMyUser(userId, userInfoRequestDto);
            if (result) return ResponseEntity.ok("수정 성공");
            else return ResponseEntity.badRequest().body("수정 실패");

        } catch (IllegalStateException e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    /**
     * 유저 아이디 찾기
     * @param userDto
     * @return
     */
    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserDto userDto) {

        String name = userDto.getName();
        String email = userDto.getEmail();

        try {
            String userId = userService.findUserId(name, email);
            return ResponseEntity.ok().body(userId);
        } catch (NoSuchElementException e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    //클라이언트가 저송한
    //비밀번호 찾기
    @PostMapping("/find-pw")
    public ResponseEntity<?> findUserPw(@RequestBody UserDto userDTO ) {
        String id = userDTO.getId();
        String name = userDTO.getName();
        String email = userDTO.getEmail();

        try {
            String userPw = userService.findUserPw(id,name, email);
            return ResponseEntity.ok().body("찾은 비밀번호 : " + userPw);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("입력한 아이디, 이름과 이메일을 다시 확인해주세요.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
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
            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (DataIntegrityViolationException e) {

            errorMessage.setMsg(e.getMessage());
            errorMessage.setErrorCode(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        } catch (Exception e) {

            errorMessage.setMsg("서버 오류 발생: " + e.getMessage());
            errorMessage.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


}


