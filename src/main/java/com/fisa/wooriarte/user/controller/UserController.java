package com.fisa.wooriarte.user.controller;


import com.fisa.wooriarte.exception.MessageException;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    // 이메일 중복 체크를 통해 중복회원 거르기
    @PostMapping("/register/users")
    //<?> : 제네릭 타입, 모든 종류의 응답 본문 반환할지 나타낸다.
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) throws Exception {
        try {
            boolean result = userService.createUser(userDTO);
            if (result) {
                return ResponseEntity.ok().body("회원가입 성공");
            } else {
                return ResponseEntity.badRequest().body("이메일 중복, 회원가입 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }


}
