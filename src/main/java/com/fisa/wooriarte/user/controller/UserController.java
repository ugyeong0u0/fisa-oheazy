package com.fisa.wooriarte.user.controller;


import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.dto.request.UserInfoRequest;
import com.fisa.wooriarte.user.service.UserService;
import org.apache.coyote.Response;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    // 아이디, 이메일 중복 체크를 통해 회원가입 진행
    @PostMapping("/user")
    //<?> : 제네릭 타입, 모든 종류의 응답 본문 반환할지 나타낸다.
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO)  {
        try {
            boolean result = userService.addUser(userDTO);
            if (result) { //result 값이 true -> 회원가입 성공
                return ResponseEntity.ok().body("회원가입 성공");
            } else { //result 값이 False -> 회원가입 실패
                return ResponseEntity.badRequest().body("회원가입 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }


    //유저 로그인한 상태 -> mypage버튼 클릭스 비밀번호 검증 화면 띄우기
    @GetMapping("/user/{id}")
    public String showMyPage() {
        return "페이지";
    }


    // 유저 마이페이지 수정하기 위한 -> 비밀번호 검증
    @PostMapping("/user/{user-id}/verify-pwd")
    public ResponseEntity<?> verifyPassword(@PathVariable(name = "user-id") Long userId, @RequestBody UserDTO userDTO)  {

        try {
            if (userService.verifyPassword(userId, userDTO.getPwd())) {
                return ResponseEntity.ok().body("비밀번호 검증 성공");
            } else {
                return ResponseEntity.badRequest().body("비밀번호 검증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }

    }

    // 유저 개인 정보 조회
    @GetMapping("/user/{user-id}/info")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "user-id") Long userId){
        try {

            UserDTO userInfo = userService.getMyUser(userId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청값 오류 혹은 찾을 수 없음");
        }
    }

    // 유저 개인 정보 수정
    @PostMapping("/user/{user-id}/info")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name = "user-id") Long userId, @RequestBody UserInfoRequest userInfoRequest) throws Exception {
        try {

            Boolean result = userService.updateMyUser(userId, userInfoRequest);
            if (result) return ResponseEntity.ok("수정 성공");
            else return ResponseEntity.badRequest().body("수정실패");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청값 오류 혹은 찾을 수 없음 ");
        }
    }


    //유저 아이디 찾기
    @PostMapping("/user/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserDTO userDTO) {
        String name = userDTO.getName();
        String email = userDTO.getEmail();

        try {
            String userId = userService.findUserId(name, email);
            return ResponseEntity.ok().body("찾은 아이디 : " + userId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력한 이름과 이메일을 다시 확인해주세요.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //클라이언트가 저송한
    //비밀번호 찾기
    @PostMapping("/user/find-pw")
    public ResponseEntity<?> findUserPw(@RequestBody UserDTO userDTO ) {
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







}


