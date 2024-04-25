package com.fisa.wooriarte.email.controller;

import com.fisa.wooriarte.email.dto.EmailCheckDto;
import com.fisa.wooriarte.email.dto.EmailRequestDto;
import com.fisa.wooriarte.email.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(EmailController.class);


    public EmailController(EmailService mailService) {
        this.emailService = mailService;
    }

    @PostMapping("/mail-send")
    public ResponseEntity<String> mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        log.info("Email verification request received.");
        log.info("Email for verification: {}", emailDto.getEmail());
        try {
            boolean isIdChecked = emailService.checkUserId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("Email sent successfully to: {}", emailDto.getEmail());
                return ResponseEntity.ok(result); // Success response
            } else {
                log.error("User ID not found: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("User ID does not exist."); // User ID not found
            }
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Email sending failed: " + e.getMessage()); // Failure response
        }
    }

    @PostMapping("/mail-auth-check")
    public ResponseEntity<?> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        try {
            boolean isIdChecked = emailService.checkUserId(emailCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
            if(isIdChecked & isAuthNumChecked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "입력 정보 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "에러 발생" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}