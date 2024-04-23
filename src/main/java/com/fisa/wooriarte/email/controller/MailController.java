package com.fisa.wooriarte.email.controller;

import com.fisa.wooriarte.email.dto.EmailCheckDto;
import com.fisa.wooriarte.email.dto.EmailRequestDto;
import com.fisa.wooriarte.email.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {
    private final MailSendService mailService;

    @PostMapping("/mail-send")
    public ResponseEntity<String> mailSend(@RequestBody @Valid EmailRequestDto emailDto){
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());
        try {
            String result = mailService.joinEmail(emailDto.getEmail());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("이메일 전송 실패: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/mail-auth-check")
    public ResponseEntity<?> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        try {
            Boolean Checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
            if(Checked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "인증 번호 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "뭔가 잘못됐습니다: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}