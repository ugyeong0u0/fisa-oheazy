package com.fisa.wooriarte.user.controller;


import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //유저 생성 test (DB 들어가는지 확인)
    //RequestBody를 달아야 postman Raw-Json 방식으로 사용이 가능하다.
    @PostMapping("/create")
    public String create(@RequestBody UserDTO userDTO) {
        userService.create(userDTO);
        System.out.println("회원가입 완료");
        return "유저 생성 완료";
    }


    // 아이디, 이메일 중복 체크를 통해 회원가입 진행
    @PostMapping("/user")
    //<?> : 제네릭 타입, 모든 종류의 응답 본문 반환할지 나타낸다.
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) throws Exception {
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
    public String showMyPage(){
        return "페이지";
    }



    // 유저 비밀번호 검증
    @PostMapping("/user/{id}/verify-pwd")
    public ResponseEntity<?> verifyPassword(@PathVariable String id, @RequestBody UserDTO userDTO) throws Exception {

        try {
            if (userService.verifyPassword(id, userDTO)) {
                return ResponseEntity.ok().body("비밀번호 검증 성공");
            } else {
                return ResponseEntity.badRequest().body("비밀번호 검증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }

    }
}
